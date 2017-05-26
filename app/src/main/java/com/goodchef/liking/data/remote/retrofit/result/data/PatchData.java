package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lennon on 16/3/12.
 */
public class PatchData extends Data
{
    @SerializedName("patch")
    private String patchUrl;
    @SerializedName("patch_id")
    private int patchId;
    @SerializedName("is_need")//客户端是否要保留该补丁 0需要 1不需要，即删除
    private int isNeed;

    private String patchFile;

    public String getPatchFile()
    {
        return patchFile;
    }

    public void setPatchFile(String patchFile)
    {
        this.patchFile = patchFile;
    }

    public String getPatchUrl()
    {
        return patchUrl;
    }

    public int getPatchId()
    {
        return patchId;
    }

    public int getIsNeed()
    {
        return isNeed;
    }

    public boolean isPatchNeed()
    {
        if (isNeed == 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "PatchData{" +
                "patchUrl='" + patchUrl + '\'' +
                ", patchId=" + patchId +
                ", isNeed=" + isNeed +
                ", patchFile='" + patchFile + '\'' +
                '}';
    }
}
