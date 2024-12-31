package com.qianxiao996.ctfknife.Encode.Modules.BrainFuck;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BFCodex 
{
	public static String mTextToBF(String pText)
	{
		int vMin = 256;
		int vMax = 0;
		Map<String, Integer> vChars = new HashMap<String, Integer>();
		List<Integer> vList = new ArrayList<Integer>();
		int vLength = pText.length();
		int vCount = 0;
		for(int vIndex = 0; vIndex < vLength; vIndex++)
		{
			char vChar = pText.charAt(vIndex);
			vMin = Math.min(vChar, vMin);
			vMax = Math.max(vChar, vMax);
			String vStringChar = new String();
			vStringChar += vChar;
			if(!vChars.containsKey(vStringChar))
			{
				vList.add((int)vChar);
				vChars.put(vStringChar, vCount);
				vCount++;
			}
		}
		String vResult = "";
//		String vResult = "BFCode for \"" + pText + "\" message : \n";
		int vHalfMin = vMin / 2;
		int vValue = vMax / vHalfMin;
		int vReste = vMax % vHalfMin;
		
		for(int vIndex = 0; vIndex < vHalfMin; vIndex++)
		{
			vResult += "+";
		}
		vResult += "[";
		for(Integer vCharValue : vList)
		{
			vResult += ">";
			int vQuotien = vCharValue / vHalfMin;
			for(int vIndex = 0; vIndex < vQuotien; vIndex++)
			{
				vResult += "+";
			}
		}
		for(int vIndex = 0; vIndex < vList.size(); vIndex++)
		{
			vResult += "<";
		}
		vResult += "-]";
		for(Integer vCharValue : vList)
		{
			vResult += ">";
			int vCharReste = vCharValue % vHalfMin;
			for(int vIndex = 0; vIndex < vCharReste; vIndex++)
			{
				vResult += "+";
			}
		}
		for(int vIndex = 0; vIndex < vList.size() - 1; vIndex++)
		{
			vResult += "<";
		}
		int vCurrentPosition = 0;
		for(int vIndex = 0; vIndex < pText.length(); vIndex++)
		{			
			String vChar = "";
			vChar += pText.charAt(vIndex);
			int vPosition = vChars.get(vChar);
			if(vPosition > vCurrentPosition)
			{
				int vGoal = vPosition - vCurrentPosition;
				for(int vCharIndex = 0; vCharIndex < vGoal; vCharIndex++)
				{
					vResult += ">";
					vCurrentPosition++;
				}
			}
			if(vPosition < vCurrentPosition)
			{
				int vGoal = vCurrentPosition - vPosition;
				for(int vCharIndex = 0; vCharIndex < vGoal; vCharIndex++)
				{
					vResult += "<";
					vCurrentPosition--;
				}
			}
			vResult += ".";
		}
		return vResult;		
	}
	
	public static String mBFToText(String pBF)
	{
		String vResult = "";
//		String vResult = "Message for BF code : " + pBF + "\n";
		String vErrorMessage = "";
		int vLoopsCount = 0;
		List<Integer> vBegins = new ArrayList<Integer>();
		List<BFLoop> vLoops = new ArrayList<BFLoop>();
		for(int vIndex = 0; vIndex < pBF.length(); vIndex++)
		{
			char vChar = pBF.charAt(vIndex);
			switch(vChar)
			{
				case '[':
				{
					vLoopsCount++;
					vBegins.add(vIndex);
				}break;
				case ']':
				{
					vLoopsCount--;
					if(vLoopsCount < 0)
					{
						return "Error at " + vIndex + " : closing brace ']' without previous oppening brace '['.";
					}
					vLoops.add(new BFLoop(vBegins.get(vBegins.size() - 1), vIndex));
					vBegins.remove(vBegins.size() - 1);
				}break;
			}
		}
		if(vLoopsCount > 0)
		{
			return "Error missing closing brace ']' for oppening brace at " + vBegins.get(vBegins.size());
		}
		int vInstructionPointer = 0;
		int vMemoryPointer = 0;
		String vConsole = "";
		List<Character> vMemory = new ArrayList<Character>();
		vMemory.add(new Character((char) 0));
		while(vInstructionPointer < pBF.length())
		{
			switch(pBF.charAt(vInstructionPointer))
			{
				case '[':
				{
					if(vMemory.get(vMemoryPointer) == 0)
					{
						for(BFLoop vLoop : vLoops)
						{
							if(vLoop.mBegin() == vInstructionPointer)
							{
								vInstructionPointer = vLoop.mEnd();
							}
						}
					}
				}break;
				case ']':
				{
					for(BFLoop vLoop : vLoops)
					{
						if(vLoop.mEnd() == vInstructionPointer)
						{
							vInstructionPointer = vLoop.mBegin()-1;
						}
					}
				}break;
				case '>':
				{
					vMemoryPointer++;
					if(vMemoryPointer >= vMemory.size())
					{
						vMemory.add(new Character((char)0));
					}
				}break;
				case '<':
				{
					vMemoryPointer--;
					if(vMemoryPointer < 0)
					{
						vMemory.add(new Character((char)0));
						vMemoryPointer++;
					}
				}break;
				case '+':
				{
					vMemory.set(vMemoryPointer, (char) (vMemory.get(vMemoryPointer) + 1));
				}break;
				case '-':
				{
					vMemory.set(vMemoryPointer, (char) (vMemory.get(vMemoryPointer) - 1));
				}break;
				case '.':
				{
					vConsole += vMemory.get(vMemoryPointer);
				}break;
				case ',':
				{
					try 
					{
						vMemory.set(vMemoryPointer, (char)System.in.read());
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}break;
			}
			vInstructionPointer++;
		}
		vResult += vConsole;
//		vResult += "\n------\nMemory:\n";
//
//		for(int vIndex = 0; vIndex < vMemory.size(); vIndex++)
//		{
//			vResult += vIndex + ": '";
//			vResult += (char)vMemory.get(vIndex);
//			vResult += "';\n";
//		}
		return vResult;
	}
}
