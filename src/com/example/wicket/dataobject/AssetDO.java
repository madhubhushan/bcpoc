package com.example.wicket.dataobject;

import java.io.Serializable;

public abstract class AssetDO implements Serializable
{
	private static final long serialVersionUID = -4216169525289932108L;
	
	protected String assetId;

	public abstract String getAssetId();
	public abstract void setAssetId(String assetId);
	
	@Override
	public boolean equals(Object o)
	{
		return o != null && o instanceof AssetDO && ((AssetDO) o).getAssetId().equals(this.getAssetId());
	}
}
