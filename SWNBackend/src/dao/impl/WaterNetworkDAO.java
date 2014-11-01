package dao.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.Asset;

public class WaterNetworkDAO
{
	private WaterNetworkDAO()
	{
	}

	private volatile static List<Node> rootNodes;

	/**
	 * Read the entire network from the network database. Implemented as a
	 * singleton to avoid DB access each time.
	 * 
	 * @return A list of trees with each tree representing a part of the water
	 *         network being rooted either at a source or a storage asset
	 */
	public List<Node> findNetworkRoots()
	{
		// double locking implementation
		if (rootNodes == null)
		{
			synchronized (WaterNetworkDAO.class)
			{
				if (rootNodes == null)
				{
					// Find all storage assets.
					List<Asset> rootAssets = new AssetDAO()
							.findByType("storage");

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
					for (Asset rootAsset : rootAssets)
						rootNodes.add(getSourceTree(rootAsset));
				}
			}
		}
		return rootNodes;
	}

	/**
	 * Constructs a network tree with assets to whom water flows from the given
	 * source asset
	 * 
	 * @param rootAsset
	 *            Identifier of the source asset
	 * @return A tree with the source asset at its root
	 */
	private Node getSourceTree(Asset rootAsset)
	{

		Queue<Node> worklist = new LinkedList<>();
		Node root = new Node(rootAsset, null);
		worklist.add(root);
		while (!worklist.isEmpty())
		{
			Node fromNode = worklist.remove();
			List<Asset> toAssets = (new AssetDAO())
					.findConnectedAssets(fromNode.getAsset().getId());

			List<Node> children = new LinkedList<>();
			for (Asset toAsset : toAssets)
			{
				Node node = new Node(toAsset, null);
				children.add(node);
				worklist.add(node);
			}
			fromNode.setChildren(children);
		}
		return root;
	}
}

class Node
{
	private Asset asset;
	private List<Node> children;

	public Node(Asset asset, List<Node> children)
	{
		super();
		this.asset = asset;
		this.children = children;
	}

	public Asset getAsset()
	{
		return asset;
	}

	public void setAsset(Asset asset)
	{
		this.asset = asset;
	}

	public List<Node> getChildren()
	{
		return children;
	}

	public void setChildren(List<Node> children)
	{
		this.children = children;
	}
}
