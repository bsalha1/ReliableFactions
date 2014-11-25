package com.massivecraft.factions.tax;

import org.bukkit.OfflinePlayer;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.integration.Econ;

public class TaxPlayer {
	public TaxPlayer(FPlayer player) {
		this.player = player;
		this.faction = TaxFaction.getTaxFaction(player.getFaction());
	}
	private final FPlayer player;
	private final TaxFaction faction;
	
	public FPlayer getPlayer() {
		return player;
	}
	public TaxFaction getFaction() {
		return faction;
	}
	public static TaxPlayer getTaxPlayer(FPlayer player) {
		//TODO cache
		return new TaxPlayer(player);
	}
	
	public static TaxPlayer getTaxPlayer(OfflinePlayer player) {
		return getTaxPlayer(FPlayers.getInstance().getByOfflinePlayer(player));
	}
	
	public double getTax() {
		TaxRules taxRules = getFaction().getTaxRules();
		if (taxRules.getPlayerTaxMap().containsKey(getPlayer().getPlayer().getUniqueId())) {
			return taxRules.getPlayerTaxMap().get(getPlayer().getPlayer().getUniqueId());
		} else if (taxRules.getRoleTaxMap().containsKey(getPlayer().getRole())) {
			return taxRules.getRoleTaxMap().get(getPlayer().getRole());
		} else {
			return taxRules.getDefaultTax();
		}
	}
	
	public boolean canAffordTax(int taxPeriods) {
		double owedTax = getTax() * taxPeriods;
		return getBalance() > owedTax;
	}
	
	public double getBalance() {
		return Econ.getBalance(getPlayer().getAccountId());
	}
	
	public void msg(String msg, Object... args) {
		getPlayer().msg(msg, args);
	}
	
	public void msg(String[] msgs) {
		for (String msg : msgs) {
			msg(msg);
		}
	}
}
