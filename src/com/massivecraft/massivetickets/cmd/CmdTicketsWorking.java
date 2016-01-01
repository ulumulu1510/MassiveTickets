package com.massivecraft.massivetickets.cmd;

import org.bukkit.ChatColor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivetickets.MassiveTickets;
import com.massivecraft.massivetickets.Perm;
import com.massivecraft.massivetickets.entity.MConf;

public class CmdTicketsWorking extends MassiveTicketsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdTicketsWorking()
	{
		// Parameters
		this.addParameter(TypeBoolean.getYes(), "yes/no", "*toggle*");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.WORKING.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		boolean before = msender.isWorking();
		boolean after = this.readArg(!before);
		
		// Detect Nochange
		if (before == after)
		{
			String message;
			Mson button;
			String commandLine;
			
			if (after)
			{
				commandLine = this.getCommandLine("no");
				message = "You are already working. ";
				button = BUTTON_STOP;
			}
			else
			{
				commandLine = this.getCommandLine("yes");
				message = "You are already not working. ";
				button = BUTTON_START;
			}
			
			Mson mson = mson(
				message,
				button.command(commandLine)
			).color(ChatColor.YELLOW);
			
			message(mson);
			return;
		}
		
		// Apply
		msender.setWorking(after);
		
		// Inform
		String verb = after ? "started" : "stopped"; 
		MassiveTickets.alertOneMsg(msender.getId(), "You %s working!", verb);
		MassiveTickets.alertOneMessage(msender.getId(), MassiveTickets.createBumpMessage());
		
		// React
		if (after)
		{
			MConf.get().getWorkingOnReaction().run(msender.getId(), null);
		}
		else
		{
			MConf.get().getWorkingOffReaction().run(msender.getId(), null);
		}
		
	}
	
}
