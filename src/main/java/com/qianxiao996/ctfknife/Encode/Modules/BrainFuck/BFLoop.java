package com.qianxiao996.ctfknife.Encode.Modules.BrainFuck;

public class BFLoop
{
	private int aBegin;
	private int aEnd;
	
	public BFLoop(int pBegin, int pEnd)
	{
		this.aBegin = pBegin;
		this.aEnd = pEnd;
	}
	
	public int mBegin()
	{
		return this.aBegin;
	}
	
	public int mEnd()
	{
		return this.aEnd;
	}
}
