package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dao.impl.AssetDAO;

/**
 * Represents the smart water network. Implemented as a singleton to avoid DB
 * access each time.
 */
public class WaterNetwork
{
	// volatile to ensure that the JVM does not reorder assignments to rootNodes
	private volatile static WaterNetwork waterNetwork;

	// A list of trees with each tree representing a part of the water network
	// being rooted either at a source or a storage asset
	private List<SWNNode> rootNodes;

	public List<SWNNode> getRootNodes()
	{
		return rootNodes;
	}

	private WaterNetwork()
	{
		// Find all storage assets.
		List<Asset> rootAssets = new AssetDAO().findByType("storage");

		// If there are no storage assets search directly for
		// source assets
		if (rootAssets.isEmpty())
		{
			rootAssets = new AssetDAO().findByType("source");
			if (rootAssets.isEmpty())
				throw new RuntimeException(
						"There are no sources or storages in the network database");
		}

		// Construct the network tree rooted out of each
		// storage/source
		rootNodes = new LinkedList<>();
		for (Asset rootAsset : rootAssets)
		{
			Queue<SWNNode> worklist = new LinkedList<>();
			SWNNode root = new SWNNode(rootAsset, null);
			worklist.add(root);
			while (!worklist.isEmpty())
			{
				SWNNode fromSWNNode = worklist.remove();
				List<Asset> toAssets = (new AssetDAO())
						.findConnectedAssets(fromSWNNode.getAsset().getId());

				List<SWNNode> children = new LinkedList<>();
				for (Asset toAsset : toAssets)
				{
					SWNNode node = new SWNNode(toAsset, null);
					children.add(node);
					worklist.add(node);
				}
				fromSWNNode.setChildren(children);
			}
			rootNodes.add(root);
		}
	}

	public static WaterNetwork getInstance()
	{
		// double locking implementation to deal with multithreaded requests
		if (waterNetwork == null)
		{
			synchronized (WaterNetwork.class)
			{
				if (waterNetwork == null)
					waterNetwork = new WaterNetwork();
			}
		}
		return waterNetwork;
	}

	/**
	 * Get all nodes through which water enters into the specified aggregation
	 * 
	 * @param aggregationId
	 *            The id of the specified aggregation
	 * @return List of above described nodes
	 */
	public List<SWNNode> getEntryNodes(int aggregationId)
	{
		List<SWNNode> entryNodes = new LinkedList<>();

		for (SWNNode rootNode : getRootNodes())
		{
			entryNodes.addAll(getEntryNodes(aggregationId, rootNode));
			if (!entryNodes.isEmpty())
				break; // Aggregations cannot be described across network trees
		}
		return entryNodes;
	}

	/**
	 * Get all entry nodes if the specified aggregation is defined in the water
	 * network tree rooted at the specified root node
	 * 
	 * @param aggregationId
	 * @param rootNode
	 * @return
	 */
	private List<SWNNode> getEntryNodes(int aggregationId, SWNNode rootNode)
	{
		List<SWNNode> entryNodes = new LinkedList<>();
		if (rootNode.getAsset().getAggregationId() == aggregationId)
			entryNodes.add(rootNode);
		else
		{
			for (SWNNode node : rootNode.getChildren())
				entryNodes.addAll(getEntryNodes(aggregationId, node));
		}

		return entryNodes;
	}
}