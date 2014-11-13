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

	public SWNNode getNode(int assetId)
	{
		for (SWNNode rootNode : WaterNetwork.getInstance().getRootNodes())
		{
			SWNNode assetNode = getNode(assetId, rootNode);
			if (assetNode != null)
				return assetNode;
		}
		return null;

	}

	public SWNNode getNode(int assetId, SWNNode rootNode)
	{
		if (rootNode.getAsset().getId() == assetId)
			return rootNode;
		else
		{
			for (SWNNode node : rootNode.getChildren())
			{
				SWNNode tmpNode = getNode(assetId, node);
				if (tmpNode != null)
					return null;
			}
			return null;
		}
	}
}