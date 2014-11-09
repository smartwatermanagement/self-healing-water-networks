package model;

import java.util.List;

/**
 * Wraps around an asset so that storage rooted trees representing the SWN can be constructed
 * @author kempa
 *
 */
public class SWNNode
{
	private Asset asset;
	private List<SWNNode> children;

	public SWNNode(Asset asset, List<SWNNode> children)
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

	public List<SWNNode> getChildren()
	{
		return children;
	}

	public void setChildren(List<SWNNode> children)
	{
		this.children = children;
	}
}
