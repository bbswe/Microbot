/*
 * Copyright (c) 2020, slaytostay
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.microbot.questhelper.helpers.quests.thegiantdwarf;

import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.questinfo.QuestVarPlayer;
import net.runelite.client.plugins.microbot.questhelper.requirements.ChatMessageRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.Conditions;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.npc.DialogRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.FreeInventorySlotRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.SkillRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.SpellbookRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.WeightRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.LogicType;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.Operation;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.Spellbook;
import net.runelite.client.plugins.microbot.questhelper.requirements.var.VarbitRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.var.VarplayerRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.widget.WidgetTextRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ExperienceReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.QuestPointReward;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.*;

import java.util.*;

@SuppressWarnings("CheckStyle")
public class TheGiantDwarf extends BasicQuestHelper
{
	//Items Required
	ItemRequirement coins2500, logs, tinderbox, coal, ironBar, lawRune, airRune, sapphires3, oresBars, redberryPie, redberryPieNoInfo,
		weightBelow30, inventorySpace, coins200, bookOnCostumes, exquisiteClothes, exquisiteBoots, dwarvenBattleaxe, leftBoot,
			dwarvenBattleaxeBroken, dwarvenBattleaxeSapphires;

	Requirement weightBelow30Check, inventorySpaceCheck;

	//Items Recommended
	ItemRequirement rellekkaTeleport, fairyRings, staminaPotions, varrockTeleport, houseTeleport, clay10,
		copperOre10, tinOre10, ironOre10, coal10, silverOre10, goldOre10, mithrilOre10,
		bronzeBar10, ironbar10, silverBar10, goldBar10, steelBar10, mithrilBar10;

	Requirement inTrollRoom, inKeldagrim, inDwarfEntrance, askedToStartMachine, talkedToVermundi,
		talkedToLibrarian, hasBookOnCostumes, talkedToVermundiWithBook, usedCoalOnMachine, startedMachine,
		hasExquisiteClothes, talkedToSaro, talkedToDromund, hasLeftBoot, hasExquisiteBoots, givenThurgoPie,
		talkedToLibrarianAboutReldo, talkedToSantiri, usedSapphires, hasDwarvenBattleaxe, givenExquisiteClothes,
		givenExquisiteBoots, givenDwarvenBattleaxe, inConsortium, completedSecretaryTasks, completedDirectorTasks,
		joinedCompany, previouslyGivenPieToThurgo, talkedToReldo;

	QuestStep enterDwarfCave, enterDwarfCave2, talkToBoatman, talkToVeldaban, talkToBlasidar, talkToVermundi,
		talkToLibrarian, climbBookcase, talkToVermundiWithBook, talkToVermundiAfterBook, useCoalOnMachine, startMachine,
		talkToVermundiWithMachine,
		talkToSaro, talkToDromund, takeLeftBoot, takeRightBoot,
		talkToSantiri, useSapphires, talkToLibrarianAboutImcando, talkToReldo, talkToThurgo, talkToThurgoAfterPie,
		giveItemsToRiki, talkToBlasidarAfterItems,
		enterConsortium, talkToSecretary, talkToDirector, joinCompany, talkToDirectorAfterJoining, leaveConsortium, talkToVeldabanAfterJoining;

	//Zones
	Zone keldagrim, keldagrim2, trollRoom, dwarfEntrance, consortium;

	@Override
	protected void setupRequirements()
	{
		// Required
		coins2500 = new ItemRequirement("coins", ItemCollections.COINS, 2500);
		coins2500.setTooltip("Bring more to be safe.");
		coins200 = new ItemRequirement("Coins", ItemCollections.COINS, 200);
		logs = new ItemRequirement("Logs", ItemID.LOGS);
		logs.setTooltip("Most logs will work, however, arctic pine logs and redwood logs do not work.");
		logs.addAlternates(ItemID.OAK_LOGS, ItemID.WILLOW_LOGS, ItemID.TEAK_LOGS, ItemID.MAPLE_LOGS,
			ItemID.MAHOGANY_LOGS, ItemID.YEW_LOGS, ItemID.MAGIC_LOGS);
		tinderbox = new ItemRequirement("Tinderbox", ItemID.TINDERBOX).isNotConsumed();
		tinderbox.setHighlightInInventory(true);
		coal = new ItemRequirement("Coal", ItemID.COAL);
		coal.setTooltip("There are rocks in the city, but you need 30 Mining.");
		coal.setHighlightInInventory(true);
		ironBar = new ItemRequirement("Iron bar", ItemID.IRON_BAR);
		ironBar.setTooltip("Purchasable during the quest.");
		lawRune = new ItemRequirement("Law rune", ItemID.LAWRUNE);
		lawRune.setTooltip("For Telekinetic Grab.");
		airRune = new ItemRequirement("Air rune", ItemID.AIRRUNE);
		airRune.setTooltip("For Telekinetic Grab.");
		sapphires3 = new ItemRequirement("Cut sapphires", ItemID.SAPPHIRE, 3);
		sapphires3.setTooltip("Purchasable during the quest.");
		sapphires3.setHighlightInInventory(true);
		oresBars = new ItemRequirement("Various ores and bars", -1, -1);
		oresBars.canBeObtainedDuringQuest();
		redberryPie = new ItemRequirement("Redberry pie", ItemID.REDBERRY_PIE);
		redberryPie.setTooltip("Unless you have previously given Thurgo an extra pie with nothing in return.");
		redberryPieNoInfo = new ItemRequirement("Redberry pie", ItemID.REDBERRY_PIE);

		// Recommended
		houseTeleport = new ItemRequirement("A house teleport if your house is in Rimmington and if you have no faster way to Mudskipper Point", -1, -1);
		rellekkaTeleport = new ItemRequirement("A Camelot/Rellekka teleport (for starting the quest)", ItemID.MAGIC_STRUNG_LYRE_5);
		rellekkaTeleport.addAlternates(ItemID.MAGIC_STRUNG_LYRE_4, ItemID.MAGIC_STRUNG_LYRE_3, ItemID.MAGIC_STRUNG_LYRE_2,
			ItemID.MAGIC_STRUNG_LYRE, ItemID.NZONE_TELETAB_RELLEKKA, ItemID.POH_TABLET_CAMELOTTELEPORT);
		rellekkaTeleport.addAlternates(ItemCollections.SLAYER_RINGS);
		fairyRings = new ItemRequirement("Access to fairy rings", -1, -1).isNotConsumed();
		fairyRings.setDisplayItemId(ItemID.POH_SUPERIOR_GARDEN_TELEPORT_RING);
		staminaPotions = new ItemRequirement("Some stamina potions (when collecting the ores)", ItemCollections.STAMINA_POTIONS);
		varrockTeleport = new ItemRequirement("A ring of wealth/amulet of glory/Varrock teleport", ItemID.POH_TABLET_VARROCKTELEPORT);
		varrockTeleport.addAlternates(ItemCollections.RING_OF_WEALTHS);
		varrockTeleport.addAlternates(ItemCollections.AMULET_OF_GLORIES);
		clay10 = new ItemRequirement("Clay", ItemID.CLAY, 10);
		copperOre10 = new ItemRequirement("Copper ore", ItemID.COPPER_ORE, 10);
		tinOre10 = new ItemRequirement("Tin ore", ItemID.TIN_ORE, 10);
		ironOre10 = new ItemRequirement("Iron ore", ItemID.IRON_ORE, 10);
		coal10 = new ItemRequirement("Coal", ItemID.COAL, 10);
		silverOre10 = new ItemRequirement("Silver ore", ItemID.SILVER_ORE, 10);
		goldOre10 = new ItemRequirement("Gold ore", ItemID.GOLD_ORE, 10);
		mithrilOre10 = new ItemRequirement("Mithril ore", ItemID.MITHRIL_ORE, 10);
		bronzeBar10 = new ItemRequirement("Bronze bar", ItemID.BRONZE_BAR, 10);
		ironbar10 = new ItemRequirement("Iron bar", ItemID.IRON_BAR, 10);
		silverBar10 = new ItemRequirement("Silver bar", ItemID.SILVER_BAR, 10);
		goldBar10 = new ItemRequirement("Gold bar", ItemID.GOLD_BAR, 10);
		steelBar10 = new ItemRequirement("Steel bar", ItemID.STEEL_BAR, 10);
		mithrilBar10 = new ItemRequirement("Mithril bar", ItemID.MITHRIL_BAR, 10);

		// Extra
		weightBelow30 = new ItemRequirement("Weight below 30 kg", -1, -1);
		inventorySpace = new ItemRequirement("Free inventory slot", -1);

		weightBelow30Check = new WeightRequirement("Weight below 30 kg", 30, Operation.LESS_EQUAL);
		inventorySpaceCheck = new FreeInventorySlotRequirement(1);

		// Quest
		bookOnCostumes = new ItemRequirement("Book on costumes", ItemID.DWARF_LIBRARY_BOOK);
		exquisiteClothes = new ItemRequirement("Exquisite clothes", ItemID.DWARF_CLOTHES);
		exquisiteBoots = new ItemRequirement("Exquisite boots", ItemID.DWARF_PERFECT_PAIR_OF_BOOTS);
		dwarvenBattleaxe = new ItemRequirement("Dwarven battleaxe", ItemID.DWARF_BATTLEAXE_NEW);

		leftBoot = new ItemRequirement("Left boot", ItemID.DWARF_PERFECT_LEFT_BOOT);

		dwarvenBattleaxeBroken = new ItemRequirement("Dwarven battleaxe", ItemID.DWARF_BATTLEAXE_OLD);
		dwarvenBattleaxeBroken.addAlternates(ItemID.DWARF_BATTLEAXE_SHARPENED);
		dwarvenBattleaxeBroken.setHighlightInInventory(true);

		dwarvenBattleaxeSapphires = new ItemRequirement("Dwarven battleaxe", ItemID.DWARF_BATTLEAXE_SAPPHIRES);
	}

	@Override
	protected void setupZones()
	{
		trollRoom = new Zone(new WorldPoint(2762, 10123, 0), new WorldPoint(2804, 10164, 0));
		dwarfEntrance = new Zone(new WorldPoint(2814, 10121, 0), new WorldPoint(2884, 10139, 0));
		keldagrim = new Zone(new WorldPoint(2816, 10177, 0), new WorldPoint(2943, 10239, 0));
		keldagrim2 = new Zone(new WorldPoint(2901, 10150, 0), new WorldPoint(2943, 10177, 0));
		consortium = new Zone(new WorldPoint(2861, 10186, 1), new WorldPoint(2897, 10212, 1));
	}

	public void setupConditions()
	{
		inTrollRoom = new ZoneRequirement(trollRoom);
		inDwarfEntrance = new ZoneRequirement(dwarfEntrance);
		inKeldagrim = new ZoneRequirement(keldagrim, keldagrim2);

		// On boat
		// 575 0->1
		// 579 0->1

		// Arrived on boat:
		// 577 0->9
		// 578 0->9

		// Blasidar said about first topic:
		// 573 0->1

		talkedToVermundi = new Conditions(true, LogicType.OR,
			new DialogRequirement("Great, thanks a lot, I'll check out the library!"),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I should speak to the " +
				"<col=800000>librarian<col=000080> in Keldagrim-West. He"),
			new WidgetTextRequirement(219, 1, 2, "Yes, about those special clothes again..."));

		talkedToLibrarian = new Conditions(true, LogicType.OR,
			new WidgetTextRequirement(InterfaceID.ChatLeft.TEXT,
				"Let me think... I believe it is on the top shelf of one of<br>the bookcases in the library, because it is such an old<br>book.",
				"Well, thanks, I'll have a look."
			),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>library of Keldagrim-West. I should ")
		);

		hasBookOnCostumes = new Conditions(true, LogicType.OR,
			bookOnCostumes,
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>with the <col=800000>book on dwarven costumes<col=000080> that I got from the")
		);

		talkedToVermundiWithBook = new VarbitRequirement(584, 1);

		askedToStartMachine = new Conditions(true, LogicType.OR,
			new DialogRequirement(questHelperPlugin.getPlayerStateManager().getPlayerName(),
				"Don't worry, I'll get them for you. Let's see... some coal and some logs. Shouldn't be too hard.", false),
			new DialogRequirement("Well, like I said, I can't do anything really without my spinning machine."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I must get <col=800000>coal<col=000080> and <col=800000>logs<col=000080>.")
		);

		usedCoalOnMachine = new Conditions(true, LogicType.OR,
			new DialogRequirement("it needs to be powered up."),
			new ChatMessageRequirement("You load the spinning machine with coal and logs."),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I have to start up the dwarven <col=800000>spinning machine<col=000080> in the"));

		startedMachine = new Conditions(true, LogicType.OR,
			new ChatMessageRequirement("...and successfully start the engine!"),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I should ask <col=800000>Vermundi<col=000080>, the owner of the <col=800000>clothes stall<col=000080> in the"));

		givenExquisiteClothes = new Conditions(true, new VarbitRequirement(VarbitID.GIANTDWARF_MODEL_STATE, true, 0));
		hasExquisiteClothes = new Conditions(true, LogicType.OR,
			givenExquisiteClothes,
			exquisiteClothes,
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I have the <col=800000>exquisite clothes<col=000080> that the <col=800000>sculptor<col=000080> needs. Now I"));

		talkedToSaro = new Conditions(true, LogicType.OR,
			// TODO: You need to click 'click to continue' here for the step to actually progress
			new DialogRequirement("Thanks!"),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I should seek out the <col=800000>eccentric old dwarf<col=000080> in <col=800000>Keldagrim-"),
			new DialogRequirement("I thought I already told you where to get them?")
		);

		talkedToDromund = new Conditions(true, LogicType.OR,
			// TODO: You need to click 'click to continue' here for the step to actually progress
			new DialogRequirement("Get out you pesky human! The boots are mine and"),
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I must find some way to get the <col=800000>pair of boots<col=000080> from the"),
			new DialogRequirement("Are you sure you don't want to give me those boots?"));

		hasLeftBoot = new Conditions(true, LogicType.OR,
			leftBoot,
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true,
				"<str>I have sneakily stolen one boots from the old dwarf.")
		);

		givenExquisiteBoots = new VarbitRequirement(VarbitID.GIANTDWARF_MODEL_STATE, true, 1);
		hasExquisiteBoots = new Conditions(true, LogicType.OR,
			givenExquisiteBoots,
			exquisiteBoots,
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I have the <col=800000>exquisite pair of boots<col=000080> that the <col=800000>sculptor<col=000080> needs.")
		);

		talkedToSantiri = new Conditions(true, dwarvenBattleaxeBroken);

		usedSapphires = new Conditions(true, LogicType.OR,
			new ChatMessageRequirement("Great, all it needs now is a little sharpening!"),
			dwarvenBattleaxeSapphires);

		talkedToLibrarianAboutReldo = new Conditions(true, LogicType.OR,
			new DialogRequirement("I suppose you could try Reldo"),
			new DialogRequirement("Do you think he can help me?"),
			new DialogRequirement("He lives quite a good deal closer"));
		previouslyGivenPieToThurgo = new VarplayerRequirement(QuestVarPlayer.QUEST_THE_KNIGHTS_SWORD.getId(), 3, Operation.GREATER_EQUAL);
		talkedToReldo = new Conditions(true, LogicType.OR,
			new DialogRequirement("you could try taking them some redberry pie."));

		givenThurgoPie = new VarbitRequirement(580, 1);
		// Thurgo makes axe, 2781 = 1
		givenDwarvenBattleaxe = new VarbitRequirement(VarbitID.GIANTDWARF_MODEL_STATE, true, 2);
		hasDwarvenBattleaxe = new Conditions(true, LogicType.OR,
			givenDwarvenBattleaxe,
			dwarvenBattleaxe,
			new WidgetTextRequirement(InterfaceID.Questjournal.TEXTLAYER, true, "<col=000080>I must give the <col=800000>restored battleaxe<col=000080> to <col=800000>Riki<col=000080>, the <col=800000>sculptor's"));

		inConsortium = new ZoneRequirement(consortium);

		completedSecretaryTasks = new Conditions(true, new DialogRequirement("I'm afraid I have no more work to offer you", "You should speak directly to the director."));
		completedDirectorTasks = new Conditions(true, new DialogRequirement("Have you ever considered joining"));
		joinedCompany = new Conditions(true, LogicType.OR,
			new VarbitRequirement(578, 1), // Purple Pewter
			new VarbitRequirement(578, 2), // Yellow Fortune
			new VarbitRequirement(578, 3), // Blue Opal
			new VarbitRequirement(578, 4), // Green Gem
			new VarbitRequirement(578, 5), // White Chisel
			new VarbitRequirement(578, 6), // Silver Cog
			new VarbitRequirement(578, 7), // Brown Engine
			new VarbitRequirement(578, 8), // Would be Red Axe?
			new DialogRequirement("I will not disappoint you."),
			new DialogRequirement("Come in, come in my friend!"));
	}

	public void setupSteps()
	{
		// Starting off
		enterDwarfCave = new ObjectStep(this, ObjectID.TROLLROMANCE_STRONGHOLD_EXIT_TUNNEL, new WorldPoint(2732, 3713, 0),
			"Speak to the Dwarven Boatman.");
		enterDwarfCave2 = new ObjectStep(this, ObjectID.DWARF_CAVEWALL_TUNNEL, new WorldPoint(2781, 10161, 0),
			"Speak to the Dwarven Boatman.");
		talkToBoatman = new NpcStep(this, NpcID.DWARF_CITY_BOATMAN_MINES_PREQUEST, new WorldPoint(2829, 10129, 0), "Speak to the Dwarven Boatman.");
		talkToBoatman.addDialogStep("That's a deal!");
		talkToBoatman.addDialogStep("Yes, I'm ready and don't mind it taking a few minutes.");
		talkToBoatman.addDialogStep("Yes.");
		talkToBoatman.addSubSteps(enterDwarfCave, enterDwarfCave2);

		talkToVeldaban = new NpcStep(this, NpcID.DWARF_CITY_BLACK_GUARD_LEADER, new WorldPoint(2827, 10214, 0),
			"Finish speaking to Commander Veldaban.");
		talkToVeldaban.addDialogStep("Yes, I will do this.");

		talkToBlasidar = new NpcStep(this, NpcID.DWARF_CITY_SHOP_SCULPTURE, new WorldPoint(2907, 10205, 0),
			"Talk to Blasidar the sculptor in the east of Keldagrim.");
		talkToBlasidar.addDialogStep("Yes, I will do this.");

		// Clothes fit for a king
		talkToVermundi = new NpcStep(this, NpcID.DWARF_CITY_SHOP_CLOTH_POOR, new WorldPoint(2887, 10188, 0), "Talk to Vermundi west of " +
			"Blasidar. If you already have, open the Quest Journal to re-sync.");
		talkToVermundi.addDialogStep("Yes, I'm looking for some special clothes.");

		talkToLibrarian = new NpcStep(this, NpcID.DWARF_CITY_LIBRARIAN, new WorldPoint(2861, 10226, 0), "Talk to the Librarian.");
		talkToLibrarian.addDialogStep("Do you know anything about King Alvis' clothes?");

		climbBookcase = new ObjectStep(this, ObjectID.DWARF_KELDAGRIM_BOOKCASE_LADDER, new WorldPoint(2859, 10228, 0),
			"Climb any bookcase to find a book on costumes.", weightBelow30Check);

		talkToVermundiWithBook = new NpcStep(this, NpcID.DWARF_CITY_SHOP_CLOTH_POOR, new WorldPoint(2887, 10188, 0),
			"Talk to Vermundi with the book on costumes.", bookOnCostumes);
		talkToVermundiWithBook.addDialogStep("Yes, about those special clothes again...");

		talkToVermundiAfterBook = new NpcStep(this, NpcID.DWARF_CITY_SHOP_CLOTH_POOR, new WorldPoint(2887, 10188, 0),
			"Talk to Vermundi again.");
		talkToVermundiAfterBook.addDialogStep("Yes, about those special clothes again...");
		talkToVermundiWithBook.addSubSteps(talkToVermundiAfterBook);

		useCoalOnMachine = new ObjectStep(this, ObjectID.DWARF_KELDAGRIM_SPINNING_MACHINE, new WorldPoint(2885, 10189, 0),
			"Use your coal on the spinning machine light it with a tinderbox.", coal, logs);
		useCoalOnMachine.addIcon(ItemID.COAL);

		startMachine = new ObjectStep(this, ObjectID.DWARF_KELDAGRIM_SPINNING_MACHINE, new WorldPoint(2885, 10189, 0),
			"Start the spinning machine with a tinderbox.", tinderbox);
		startMachine.addIcon(ItemID.TINDERBOX);

		talkToVermundiWithMachine = new NpcStep(this, NpcID.DWARF_CITY_SHOP_CLOTH_POOR, new WorldPoint(2887, 10188, 0),
			"Talk to Vermundi again and pay 200gp.", coins200);
		talkToVermundiWithMachine.addDialogStep("Yes, about those special clothes again...");
		talkToVermundiWithMachine.addDialogStep("I'll pay.");

		// Boots fit for a king
		talkToSaro = new NpcStep(this, NpcID.DWARF_CITY_SHOP_ARMOUR, new WorldPoint(2827, 10198, 0), "Talk to Saro in West Keldagrim.");
		talkToSaro.addDialogStep("Yes, I'm looking for a pair of special boots.");

		talkToDromund = new NpcStep(this, NpcID.DWARF_CITY_EXCENTRIC_DWARF, new WorldPoint(2838, 10224, 0), "Talk to Dromund.");

		takeLeftBoot = new DetailedQuestStep(this, new WorldPoint(2838, 10220, 0), "Take the Left boot when Dromund isn't looking.");

		takeRightBoot = new DetailedQuestStep(this, new WorldPoint(2836, 10226, 0), "Take the Right boot from outside" +
			" his window when he isn't looking using Telekinetic Grab.", lawRune, airRune, inventorySpaceCheck);

		// An axe fit for a king
		talkToSantiri = new NpcStep(this, NpcID.DWARF_CITY_SHOP_WEAPONS, new WorldPoint(2828, 10231, 0), "Talk to Santiri.");
		talkToSantiri.addDialogStep("Yes, I'm looking for a particular battleaxe.");
		talkToSantiri.addDialogStep("Blasidar the sculptor needs it for his statue.");
		talkToSantiri.addDialogStep("Perhaps I can repair the axe?");

		useSapphires = new DetailedQuestStep(this, "Use the 3 sapphires on the axe.", dwarvenBattleaxeBroken, sapphires3);

		talkToLibrarianAboutImcando = new NpcStep(this, NpcID.DWARF_CITY_LIBRARIAN, new WorldPoint(2861, 10226, 0), "Talk to the" +
			" Librarian about Imcando Dwarves. If you already have and no option appears for this, go talk to Reldo " +
			"in Varrock Castle.");
		talkToLibrarianAboutImcando.addDialogSteps("Can you help me find an Imcando dwarf?");
		talkToLibrarianAboutImcando.conditionToHideInSidebar(previouslyGivenPieToThurgo);

		talkToReldo = new NpcStep(this, NpcID.RELDO_NORMAL, new WorldPoint(3211, 3494, 0),
			"Talk to Reldo in Varrock Castle's library.");
		talkToReldo.addDialogSteps("Ask about Imcando dwarves.");
		talkToReldo.conditionToHideInSidebar(previouslyGivenPieToThurgo);

		talkToThurgo = new NpcStep(this, NpcID.THURGO, new WorldPoint(3001, 3144, 0),
			"Talk to Thurgo at Mudskipper Point.", redberryPieNoInfo, ironBar, dwarvenBattleaxeSapphires);
		talkToThurgo.addDialogSteps("Something else.");
		talkToThurgo.addDialogStep(2, "Would you like a redberry pie?");
		talkToThurgo.addDialogStep(3, "Would you like a redberry pie?");
		talkToThurgo.addDialogStep("Return to Keldagrim immediately.");

		talkToThurgoAfterPie = new NpcStep(this, NpcID.THURGO, new WorldPoint(3001, 3144, 0),
			"Talk to Thurgo at Mudskipper Point.", ironBar, dwarvenBattleaxeSapphires);
		talkToThurgoAfterPie.addDialogStep("Something else.");
		talkToThurgoAfterPie.addDialogStep("Can you help me with this ancient axe?");
		talkToThurgoAfterPie.addDialogStep("Can you repair that axe now?");
		talkToThurgoAfterPie.addDialogStep("Return to Keldagrim immediately.");

		// Halfway there
		giveItemsToRiki = new NpcStep(this, NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL, new WorldPoint(2904, 10207, 0),
			"Talk to Riki the sculptor's model to give him the clothes, axe and boots.",
			exquisiteClothes.hideConditioned(givenExquisiteClothes), exquisiteBoots.hideConditioned(givenExquisiteBoots),
			dwarvenBattleaxe.hideConditioned(givenDwarvenBattleaxe));
		giveItemsToRiki.addDialogStep("Return to Keldagrim immediately.");
		((NpcStep) giveItemsToRiki).addAlternateNpcs(NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL_BOOTS, NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL_TORSO,
			NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL_AXE, NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL_BOOTS_AND_TORSO, NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL_BOOTS_AND_AXE,
			NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL_TORSO_AND_AXE, NpcID.DWARF_CITY_SHOP_SCULPTURE_MODEL_FINISHED);
		talkToBlasidarAfterItems = new NpcStep(this, NpcID.DWARF_CITY_SHOP_SCULPTURE, new WorldPoint(2907, 10205, 0),
			"Talk to Blasidar the sculptor.");

		// Joining the consortium
		enterConsortium = new ObjectStep(this, ObjectID.DWARF_KELDAGRIM_WIDE_STAIRS_LOWER, new WorldPoint(2895, 10210, 0),
			"Go to the upper floor of the market.");

		talkToSecretary = new NpcStep(this, NpcID.DWARF_CITY_SECRETARY_BLUE_OPAL, new WorldPoint(2869, 10205, 1),
			"Keep talking to the same secretary and complete the tasks given. If you don't want to do one of the tasks, " +
				"just talk to them again for a different one.");
		//TODO: Add a way to check which company is chosen
		//((NpcStep) talkToSecretary).addAlternateNpcs(NpcID.DWARF_CITY_SECRETARY_PURPLE_PEWTER, NpcID.DWARF_CITY_SECRETARY_GREEN_GEMSTONE,
		// NpcID.DWARF_CITY_SECRETARY_SILVER_COG, NpcID.DWARF_CITY_SECRETARY_WHITE_CHISEL);
		talkToSecretary.addDialogStep("Is there anything I can help you with?");
		talkToSecretary.addDialogStep("I'll take it.");
		talkToSecretary.addDialogStep("Do you have another task for me?");

		talkToDirector = new NpcStep(this, NpcID.DWARF_CITY_DIRECTOR_BLUE_OPAL, new WorldPoint(2879, 10199, 1),
			"Keep talking to the director of the same secretary and complete the tasks given. If you don't want to do one of the task, " +
				"just talk to them again for a different one.");
		((NpcStep) talkToDirector).addAlternateNpcs(NpcID.DWARF_CITY_DIRECTOR_BLUE_OPAL_CUTSCENE);
		talkToDirector.addDialogStep("Do you have any more tasks for me?");
		talkToDirector.addDialogStep("I'll take it.");

		joinCompany = new NpcStep(this, NpcID.DWARF_CITY_DIRECTOR_BLUE_OPAL, new WorldPoint(2879, 10199, 1),
			"Talk to the director to join the company.");
		((NpcStep) joinCompany).addAlternateNpcs(NpcID.DWARF_CITY_DIRECTOR_BLUE_OPAL_CUTSCENE);
		joinCompany.addDialogStep("I'd like to officially join your company.");

		talkToDirectorAfterJoining = new NpcStep(this, NpcID.DWARF_CITY_DIRECTOR_BLUE_OPAL, new WorldPoint(2879, 10199, 1),
			"Talk to the director after joining the company.");
		((NpcStep) talkToDirectorAfterJoining).addAlternateNpcs(NpcID.DWARF_CITY_DIRECTOR_BLUE_OPAL_CUTSCENE);
		talkToDirectorAfterJoining.addDialogStep("Blasidar the sculptor has sent me.");
		talkToDirectorAfterJoining.addDialogStep("I would support you.");
		//TODO: Make this conditional for the company chosen
		talkToDirectorAfterJoining.addDialogStep("Yes! Long live the Blue Opal!");

		leaveConsortium = new ObjectStep(this, ObjectID.DWARF_KELDAGRIM_WIDE_STAIRS_UPPER, new WorldPoint(2863, 10210, 1),
			"Go down the stairs.");

		talkToVeldabanAfterJoining = new NpcStep(this, NpcID.DWARF_CITY_BLACK_GUARD_LEADER, new WorldPoint(2827, 10214, 0),
			"Talk to Commander Veldaban.");
		talkToVeldabanAfterJoining.addSubSteps(leaveConsortium);
		talkToVeldabanAfterJoining.addDialogStep("I'm ready.");
	}

	@Override
	public List<String> getNotes()
	{
		return Collections.singletonList("This helper may occasionally go out of sync with the quest. If you think it is, open " +
			"the Quest Journal to re-sync.");
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return Arrays.asList(coins2500, logs, tinderbox, coal, ironBar, lawRune, airRune, sapphires3, oresBars, redberryPie);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return Arrays.asList(houseTeleport, rellekkaTeleport, fairyRings, staminaPotions, varrockTeleport, clay10, copperOre10, tinOre10, ironOre10, coal10, silverOre10, goldOre10, mithrilOre10, bronzeBar10, ironbar10, silverBar10, goldBar10, steelBar10, mithrilBar10);
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Starting out", Arrays.asList(talkToBoatman, talkToVeldaban, talkToBlasidar)));
		allSteps.add(new PanelDetails("Clothes fit for a king", Arrays.asList(talkToVermundi, talkToLibrarian, climbBookcase, talkToVermundiWithBook, useCoalOnMachine, startMachine, talkToVermundiWithMachine), weightBelow30, logs, coal, tinderbox, coins2500, inventorySpace));
		allSteps.add(new PanelDetails("Boots fit for a king", Arrays.asList(talkToSaro, talkToDromund, takeLeftBoot, takeRightBoot), lawRune, airRune));
		allSteps.add(new PanelDetails("An axe fit for a king", Arrays.asList(talkToSantiri, useSapphires,
			talkToLibrarianAboutImcando, talkToReldo, talkToThurgo), sapphires3, ironBar, redberryPie));
		allSteps.add(new PanelDetails("Halfway there", Arrays.asList(giveItemsToRiki, talkToBlasidarAfterItems), exquisiteClothes, exquisiteBoots, dwarvenBattleaxe));
		allSteps.add(new PanelDetails("Joining the consortium", Arrays.asList(enterConsortium, talkToSecretary, talkToDirector, joinCompany, talkToDirectorAfterJoining, talkToVeldabanAfterJoining), oresBars, staminaPotions));
		return allSteps;
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new SpellbookRequirement(Spellbook.NORMAL));
		req.add(new SkillRequirement(Skill.CRAFTING, 12));
		req.add(new SkillRequirement(Skill.FIREMAKING, 16));
		req.add(new SkillRequirement(Skill.MAGIC, 33, true));
		req.add(new SkillRequirement(Skill.THIEVING, 14, true));
		return req;
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(2);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return Arrays.asList(
				new ExperienceReward(Skill.MINING, 2500),
				new ExperienceReward(Skill.SMITHING, 2500),
				new ExperienceReward(Skill.CRAFTING, 2500),
				new ExperienceReward(Skill.MAGIC, 1500),
				new ExperienceReward(Skill.THIEVING, 1500),
				new ExperienceReward(Skill.FIREMAKING, 1500));
	}

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		// Varbit 571
		initializeRequirements();
		setupConditions();
		setupSteps();

		Map<Integer, QuestStep> steps = new HashMap<>();

		// Starting out
		ConditionalStep goToBoatman = new ConditionalStep(this, enterDwarfCave);
		goToBoatman.addStep(inTrollRoom, enterDwarfCave2);
		goToBoatman.addStep(inDwarfEntrance, talkToBoatman);
		steps.put(0, goToBoatman);
		steps.put(5, talkToVeldaban);
		steps.put(10, talkToBlasidar);

		// Clothes fit for a king
		ConditionalStep getExquisiteClothes = new ConditionalStep(this, talkToVermundi);
		getExquisiteClothes.addStep(startedMachine, talkToVermundiWithMachine);
		getExquisiteClothes.addStep(usedCoalOnMachine, startMachine);
		getExquisiteClothes.addStep(askedToStartMachine, useCoalOnMachine);
		getExquisiteClothes.addStep(talkedToVermundiWithBook, talkToVermundiAfterBook);
		getExquisiteClothes.addStep(hasBookOnCostumes, talkToVermundiWithBook);
		getExquisiteClothes.addStep(talkedToLibrarian, climbBookcase);
		getExquisiteClothes.addStep(talkedToVermundi, talkToLibrarian);

		// Boots fit for a king
		ConditionalStep getExquisiteBoots = new ConditionalStep(this, talkToSaro);
		getExquisiteBoots.addStep(hasLeftBoot, takeRightBoot);
		getExquisiteBoots.addStep(talkedToDromund, takeLeftBoot);
		getExquisiteBoots.addStep(talkedToSaro, talkToDromund);

		// An axe fit for a king
		ConditionalStep getDwarvenBattleaxe = new ConditionalStep(this, talkToSantiri);
		getDwarvenBattleaxe.addStep(new Conditions(givenThurgoPie, usedSapphires), talkToThurgoAfterPie);
		getDwarvenBattleaxe.addStep(new Conditions(previouslyGivenPieToThurgo, usedSapphires), talkToThurgo);
		// If not done Knight's Sword quest...
		getDwarvenBattleaxe.addStep(new Conditions(talkedToLibrarianAboutReldo, usedSapphires), talkToReldo);
		getDwarvenBattleaxe.addStep(usedSapphires, talkToLibrarianAboutImcando);

		getDwarvenBattleaxe.addStep(talkedToSantiri, useSapphires);

		// Halfway there
		ConditionalStep getItems = new ConditionalStep(this, getExquisiteClothes);
		getItems.addStep(new Conditions(LogicType.AND, givenExquisiteClothes, givenExquisiteBoots, givenDwarvenBattleaxe), talkToBlasidarAfterItems);
		getItems.addStep(new Conditions(LogicType.AND, hasExquisiteClothes, hasExquisiteBoots, hasDwarvenBattleaxe), giveItemsToRiki);
		getItems.addStep(new Conditions(LogicType.AND, hasExquisiteClothes, hasExquisiteBoots), getDwarvenBattleaxe);
		getItems.addStep(hasExquisiteClothes, getExquisiteBoots);

		steps.put(20, getItems);

		ConditionalStep joinConsortium = new ConditionalStep(this, enterConsortium);
		joinConsortium.addStep(new Conditions(joinedCompany, inConsortium), talkToDirectorAfterJoining);
		joinConsortium.addStep(new Conditions(completedDirectorTasks, inConsortium), joinCompany);
		joinConsortium.addStep(new Conditions(completedSecretaryTasks, inConsortium), talkToDirector);
		joinConsortium.addStep(inConsortium, talkToSecretary);

		steps.put(30, joinConsortium);

		ConditionalStep finishQuest = new ConditionalStep(this, talkToVeldabanAfterJoining);
		finishQuest.addStep(inConsortium, leaveConsortium);

		steps.put(40, finishQuest);

		return steps;
	}
}
