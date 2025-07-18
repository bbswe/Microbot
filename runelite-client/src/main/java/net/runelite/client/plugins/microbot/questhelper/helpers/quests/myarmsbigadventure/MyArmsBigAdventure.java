/*
 * Copyright (c) 2020, Zoinkwiz
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
package net.runelite.client.plugins.microbot.questhelper.helpers.quests.myarmsbigadventure;

import net.runelite.client.plugins.microbot.questhelper.bank.banktab.BankSlotIcons;
import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.questinfo.QuestHelperQuest;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.Conditions;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.NpcCondition;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemOnTileRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.SkillRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.quest.QuestRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.Operation;
import net.runelite.client.plugins.microbot.questhelper.requirements.var.VarbitRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ExperienceReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.QuestPointReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.UnlockReward;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;

import java.util.*;

public class MyArmsBigAdventure extends BasicQuestHelper
{
	//Items Required
	ItemRequirement goutLump, bucket, bucketHighlight, farmingManual, ugthanki3, rake, dibber, spade, hardyGout, superCompost,
		rakeHighlight, dibberHighlight, hardyGoutHighlight, superCompostHighlight, spadeHighlight, plantCureHighlight,
		supercompost7, cureOrCompost, rakeHead, rakeHandle, climbingBoots, superCompost8;

	//Items Recommended
	ItemRequirement food, prayerPotions, combatGear, gamesNecklace;

	Requirement inStrongholdFloor1, inStrongholdFloor2, inPrison, onRoof, added3Dung, added7Comp, usedRake, givenCompost, givenHardy, givenDibber,
		givenCure, hasRakeHeadAndHandle, rakeHeadNearby, babyNearby, giantNearby;

	DetailedQuestStep enterStronghold, goDownToChef, goUpToChef, talkToBurntmeat, talkToMyArm, useBucketOnPot, enterStrongholdWithLump, goDownToArmWithLump, goUpToArmWithLump, talkToArmWithLump,
		enterStrongholdAfterLump, goDownAfterLump, goUpAfterLump, talkToArmAfterLump, goUpFromF1ToMyArm, goUpToMyArm, talkToMyArmUpstairs, readBook, talkToMyArmAfterReading, useUgthankiDung,
		useCompost, talkToMyArmAfterFertilising, talkToBarnaby, talkToMyArmAtTai, talkToMurcaily, talkToMyArmAfterMurcaily, enterStrongholdForFight, goUpToRoofForFight, talkToMyArmForFight, giveRake,
		goUpFromF1ForFight, goUpFromPrisonForFight, giveSupercompost, giveHardyGout, giveDibber, giveCure, talkAfterBoat, pickUpRakeHead, repairRake, talkToMyArmAfterBaby,
		talkToMyArmAfterGrow, killBabyRoc, killGiantRoc, giveSpade, goDownFromMyArmToBurntmeat, goDownToBurntmeat, talkToBurntmeatAgain, goUpToMyArmFinish, talkToMyArmAfterHarvest,
		goUpFromBurntmeatFinish, talkToMyArmFinish, enterStrongholdFinish;

	//Zones
	Zone strongholdFloor1, strongholdFloor2, prison, roof;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupConditions();
		setupSteps();
		Map<Integer, QuestStep> steps = new HashMap<>();

		ConditionalStep startingOffSteps = new ConditionalStep(this, enterStronghold);
		startingOffSteps.addStep(inStrongholdFloor1, talkToBurntmeat);
		startingOffSteps.addStep(inPrison, goUpToChef);
		startingOffSteps.addStep(inStrongholdFloor2, goDownToChef);

		steps.put(0, startingOffSteps);
		steps.put(10, startingOffSteps);
		steps.put(20, startingOffSteps);
		steps.put(30, startingOffSteps);

		ConditionalStep goTalkToBurntmeat = new ConditionalStep(this, enterStronghold);
		goTalkToBurntmeat.addStep(inStrongholdFloor1, talkToMyArm);
		goTalkToBurntmeat.addStep(inPrison, goUpToChef);
		goTalkToBurntmeat.addStep(inStrongholdFloor2, goDownToChef);


		steps.put(40, goTalkToBurntmeat);
		steps.put(50, goTalkToBurntmeat);

		ConditionalStep getGout = new ConditionalStep(this, useBucketOnPot);
		getGout.addStep(new Conditions(goutLump.alsoCheckBank(questBank), inStrongholdFloor1), talkToArmWithLump);
		getGout.addStep(new Conditions(goutLump.alsoCheckBank(questBank), inStrongholdFloor2), goDownToArmWithLump);
		getGout.addStep(new Conditions(goutLump.alsoCheckBank(questBank), inPrison), goUpToArmWithLump);
		getGout.addStep(goutLump.alsoCheckBank(questBank), enterStrongholdWithLump);

		steps.put(60, getGout);

		ConditionalStep talkAfterLump = new ConditionalStep(this, enterStrongholdAfterLump);
		talkAfterLump.addStep(new Conditions(inStrongholdFloor1), talkToArmWithLump);
		talkAfterLump.addStep(new Conditions(inStrongholdFloor2), goDownToArmWithLump);
		talkAfterLump.addStep(new Conditions(inPrison), goUpAfterLump);

		steps.put(70, talkAfterLump);

		ConditionalStep talkToArmOnRoofSteps = new ConditionalStep(this, enterStrongholdAfterLump);
		talkToArmOnRoofSteps.addStep(new Conditions(onRoof), talkToMyArmUpstairs);
		talkToArmOnRoofSteps.addStep(new Conditions(inStrongholdFloor2), goUpToMyArm);
		talkToArmOnRoofSteps.addStep(new Conditions(inStrongholdFloor1), goUpFromF1ToMyArm);
		talkToArmOnRoofSteps.addStep(new Conditions(inPrison), goUpAfterLump);

		steps.put(80, talkToArmOnRoofSteps);

		steps.put(90, readBook);

		ConditionalStep readBookForArm = new ConditionalStep(this, enterStrongholdAfterLump);
		readBookForArm.addStep(new Conditions(onRoof), talkToMyArmAfterReading);
		readBookForArm.addStep(new Conditions(inStrongholdFloor2), goUpToMyArm);
		readBookForArm.addStep(new Conditions(inStrongholdFloor1), goUpFromF1ToMyArm);
		readBookForArm.addStep(new Conditions(inPrison), goUpAfterLump);

		steps.put(100, readBookForArm);

		ConditionalStep treatPatch = new ConditionalStep(this, useUgthankiDung);
		treatPatch.addStep(added3Dung, useCompost);

		steps.put(110, treatPatch);

		ConditionalStep talkToArmAfterMakingPatch = new ConditionalStep(this, enterStrongholdAfterLump);
		talkToArmAfterMakingPatch.addStep(new Conditions(onRoof), talkToMyArmAfterFertilising);
		talkToArmAfterMakingPatch.addStep(new Conditions(inStrongholdFloor2), goUpToMyArm);
		talkToArmAfterMakingPatch.addStep(new Conditions(inStrongholdFloor1), goUpFromF1ToMyArm);
		talkToArmAfterMakingPatch.addStep(new Conditions(inPrison), goUpAfterLump);

		steps.put(120, talkToArmAfterMakingPatch);
		steps.put(130, talkToArmAfterMakingPatch);
		steps.put(140, talkToArmAfterMakingPatch);

		steps.put(150, talkToBarnaby);
		steps.put(160, talkAfterBoat);

		steps.put(170, talkToMyArmAtTai);

		steps.put(180, talkToMurcaily);
		steps.put(190, talkToMurcaily);
		steps.put(200, talkToMurcaily);

		steps.put(210, talkToMyArmAfterMurcaily);

		ConditionalStep prepareToFight = new ConditionalStep(this, enterStrongholdForFight);
		prepareToFight.addStep(new Conditions(onRoof), talkToMyArmForFight);
		prepareToFight.addStep(new Conditions(inStrongholdFloor2), goUpToRoofForFight);
		prepareToFight.addStep(new Conditions(inStrongholdFloor1), goUpFromF1ForFight);
		prepareToFight.addStep(new Conditions(inPrison), goUpFromPrisonForFight);

		steps.put(220, prepareToFight);
		steps.put(230, prepareToFight);

		ConditionalStep growGout = new ConditionalStep(this, giveRake);
		growGout.addStep(new Conditions(givenHardy, givenCompost), giveDibber);
		growGout.addStep(givenCompost, giveHardyGout);
		growGout.addStep(usedRake, giveSupercompost);
		growGout.addStep(hasRakeHeadAndHandle, repairRake);
		growGout.addStep(rakeHeadNearby, pickUpRakeHead);

		steps.put(240, growGout);

		ConditionalStep dealWithSmallBird = new ConditionalStep(this, talkToMyArmAfterGrow);
		dealWithSmallBird.addStep(babyNearby, killBabyRoc);

		steps.put(250, dealWithSmallBird);

		ConditionalStep dealWithBigBird = new ConditionalStep(this, talkToMyArmAfterBaby);
		dealWithBigBird.addStep(giantNearby, killGiantRoc);

		steps.put(260, dealWithBigBird);

		steps.put(270, giveSpade);

		steps.put(280, talkToMyArmAfterHarvest);

		ConditionalStep talkBurntForEnd = new ConditionalStep(this, goDownFromMyArmToBurntmeat);
		talkBurntForEnd.addStep(inStrongholdFloor1, talkToBurntmeatAgain);
		talkBurntForEnd.addStep(inStrongholdFloor2, goDownToBurntmeat);

		steps.put(290, talkBurntForEnd);
		steps.put(300, talkBurntForEnd);

		ConditionalStep talkArmForEnd = new ConditionalStep(this, enterStrongholdFinish);
		talkArmForEnd.addStep(onRoof, talkToMyArmFinish);
		talkArmForEnd.addStep(inStrongholdFloor1, goUpFromBurntmeatFinish);
		talkArmForEnd.addStep(inStrongholdFloor2, goUpToMyArmFinish);

		steps.put(310, talkArmForEnd);

		return steps;
	}

	@Override
	protected void setupRequirements()
	{
		goutLump = new ItemRequirement("Goutweedy lump", ItemID.MYARM_LUMP);
		bucket = new ItemRequirement("Bucket", ItemID.BUCKET_EMPTY).isNotConsumed();
		bucketHighlight = new ItemRequirement("Bucket", ItemID.BUCKET_EMPTY).isNotConsumed();
		bucketHighlight.setHighlightInInventory(true);
		farmingManual = new ItemRequirement("Farming manual", ItemID.MYARM_BOOK);
		farmingManual.setTooltip("You can get another from My Arm on the Troll Stronghold roof");
		farmingManual.setHighlightInInventory(true);
		ugthanki3 = new ItemRequirement("Ugthanki dung", ItemID.FEUD_CAMEL_POOH_BUCKET, 3);
		ugthanki3.setHighlightInInventory(true);

		rake = new ItemRequirement("Rake", ItemID.RAKE).isNotConsumed();
		dibber = new ItemRequirement("Seed dibber", ItemID.DIBBER).isNotConsumed();
		spade = new ItemRequirement("Spade", ItemID.SPADE).isNotConsumed();
		superCompost = new ItemRequirement("Supercompost", ItemID.BUCKET_SUPERCOMPOST);
		hardyGout = new ItemRequirement("Hardy gout tubers", ItemID.MYARM_HARDYTUBERS);
		hardyGout.setTooltip("You can get more from Murcaily");
		combatGear = new ItemRequirement("Combat gear, preferably magic", -1, -1).isNotConsumed();
		combatGear.setDisplayItemId(BankSlotIcons.getCombatGear());
		food = new ItemRequirement("Food", ItemCollections.GOOD_EATING_FOOD, -1);
		prayerPotions = new ItemRequirement("Prayer potions", ItemCollections.PRAYER_POTIONS, -1);

		rakeHighlight = rake.highlighted();
		dibberHighlight = dibber.highlighted();
		spadeHighlight = spade.highlighted();
		superCompostHighlight = new ItemRequirement("Supercompost", ItemID.BUCKET_SUPERCOMPOST);
		superCompostHighlight.setHighlightInInventory(true);
		hardyGoutHighlight = new ItemRequirement("Hardy gout tubers", ItemID.MYARM_HARDYTUBERS);
		hardyGoutHighlight.setTooltip("You can get more from Murcaily");
		hardyGoutHighlight.setHighlightInInventory(true);
		plantCureHighlight = new ItemRequirement("Plant cure", ItemID.PLANT_CURE);
		plantCureHighlight.setHighlightInInventory(true);

		supercompost7 = new ItemRequirement("Supercompost", ItemID.BUCKET_SUPERCOMPOST, 7);
		superCompost8 = new ItemRequirement("Supercompost", ItemID.BUCKET_SUPERCOMPOST, 8);
		climbingBoots = new ItemRequirement("Climbing boots", ItemCollections.CLIMBING_BOOTS).isNotConsumed();

		cureOrCompost = new ItemRequirement("Either super/ultra compost, or a plant cure", ItemID.PLANT_CURE);
		cureOrCompost.addAlternates(ItemID.BUCKET_SUPERCOMPOST, ItemID.BUCKET_ULTRACOMPOST);

		rakeHead = new ItemRequirement("Rake head", ItemID.RAKE_HEAD);
		rakeHead.setHighlightInInventory(true);
		rakeHandle = new ItemRequirement("Rake handle", ItemID.RAKE_HANDLE);
		rakeHandle.setHighlightInInventory(true);

		gamesNecklace = new ItemRequirement("Games necklace for Burthorpe teleport",
			ItemCollections.GAMES_NECKLACES);
	}

	@Override
	protected void setupZones()
	{
		strongholdFloor1 = new Zone(new WorldPoint(2820, 10048, 1), new WorldPoint(2862, 10110, 1));
		strongholdFloor2 = new Zone(new WorldPoint(2820, 10048, 2), new WorldPoint(2862, 10110, 2));
		prison = new Zone(new WorldPoint(2822, 10049, 0), new WorldPoint(2859, 10110, 0));
		roof = new Zone(new WorldPoint(2822, 3665, 0), new WorldPoint(2838, 3701, 0));
	}

	public void setupConditions()
	{
		inStrongholdFloor1 = new ZoneRequirement(strongholdFloor1);
		inStrongholdFloor2 = new ZoneRequirement(strongholdFloor2);
		inPrison = new ZoneRequirement(prison);
		onRoof = new ZoneRequirement(roof);

		added3Dung = new VarbitRequirement(2791, 3);
		added7Comp = new VarbitRequirement(2792, 7);

		givenHardy = new VarbitRequirement(2794, 1);
		usedRake = new VarbitRequirement(2799, 6);
		givenCompost = new VarbitRequirement(2799, 7);

		givenDibber = new VarbitRequirement(VarbitID.MYARM_FAKEPATCH, 9, Operation.GREATER_EQUAL);
		givenCure = new VarbitRequirement(2798, 1);

		hasRakeHeadAndHandle = new Conditions(rakeHead, rakeHandle);
		rakeHeadNearby = new ItemOnTileRequirement(rakeHead);

		babyNearby = new NpcCondition(NpcID.MYARM_BABY_ROC);
		giantNearby = new NpcCondition(NpcID.MYARM_GIANT_ROC);
	}

	public void setupSteps()
	{
		enterStronghold = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_DOOR, new WorldPoint(2839, 3690, 0), "Enter the Troll Stronghold.");

		goDownToChef = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRSTOP, new WorldPoint(2844, 10052, 2), "Go down the south staircase.");

		goUpToChef = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRS, new WorldPoint(2853, 10107, 0), "Go up the stairs from the prison.");

		talkToBurntmeat = new NpcStep(this, NpcID.EADGAR_TROLL_CHIEF_COOK, new WorldPoint(2845, 10057, 1), "Talk to Burntmeat in the Troll Stronghold.");
		talkToBurntmeat.addSubSteps(enterStronghold, goDownToChef, goUpToChef);
		talkToBurntmeat.addDialogStep("What do you want now?");
		talkToMyArm = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2855, 10053, 1), "Talk to My Arm near Burntmeat.");
		talkToMyArm.addDialogStep("Alright, I'll lend him a hand.");

		useBucketOnPot = new ObjectStep(this, ObjectID.DEATH_TROLL_CAULDRON, new WorldPoint(2864, 3591, 0),
			"Use a bucket on the cooking pot on the Death Plateau. You can find a bucket next to the pot.", bucketHighlight);
		useBucketOnPot.addIcon(ItemID.BUCKET_EMPTY);
		useBucketOnPot.addTeleport(gamesNecklace);

		enterStrongholdWithLump = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_DOOR, new WorldPoint(2839, 3690, 0), "Return to My Arm with the goutweedy lump.", goutLump);

		goDownToArmWithLump = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRSTOP, new WorldPoint(2844, 10052, 2), "Return to My Arm with the goutweedy lump.", goutLump);

		goUpToArmWithLump = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRS, new WorldPoint(2853, 10107, 0), "Return to My Arm with the goutweedy lump.", goutLump);

		talkToArmWithLump = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2855, 10053, 1), "Return to My Arm with the goutweedy lump.", goutLump);
		talkToArmWithLump.addSubSteps(goUpToArmWithLump, goDownToArmWithLump, enterStrongholdWithLump);

		enterStrongholdAfterLump = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_DOOR, new WorldPoint(2839, 3690, 0), "Return to My Arm.");

		goDownAfterLump = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRSTOP, new WorldPoint(2844, 10052, 2), "Return to My Arm.");

		goUpAfterLump = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRS, new WorldPoint(2853, 10107, 0), "Return to My Arm.");

		talkToArmAfterLump = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2855, 10053, 1), "Return to My Arm.");

		talkToArmWithLump.addSubSteps(goUpToArmWithLump, goDownToArmWithLump, enterStrongholdWithLump, goUpAfterLump, goDownAfterLump, enterStrongholdAfterLump, talkToArmAfterLump);

		goUpFromF1ToMyArm = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRS, new WorldPoint(2843, 10052, 1), "Go up to the roof of the Stronghold and talk to My Arm.");
		goUpToMyArm = new ObjectStep(this, ObjectID.MYARM_LADDER, new WorldPoint(2831, 10077, 2), "Climb back up to My Arm.");
		talkToMyArmUpstairs = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2835, 3694, 0), "Talk to My Arm on the roof of the Troll Stronghold.");
		talkToMyArmUpstairs.addSubSteps(goUpFromF1ToMyArm, goUpToMyArm);

		readBook = new DetailedQuestStep(this, "Read the farming manual.", farmingManual);
		talkToMyArmAfterReading = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2835, 3694, 0), "Talk to My Arm on the roof of the Troll Stronghold.");
		useUgthankiDung = new AddDung(this);
		useCompost = new AddCompost(this);

		talkToMyArmAfterFertilising = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2835, 3694, 0), "Talk to My Arm on the roof of the Troll Stronghold.");

		talkToBarnaby = new NpcStep(this, NpcID.MYARM_BARNABY_SHIP, new WorldPoint(2683, 3275, 0), "Talk to Captain Barnaby on Ardougne dock.");
		talkToBarnaby.addDialogStep("This is My Arm. We'd like to go to Karamja.");

		talkAfterBoat = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2772, 3223, 0), "Talk to My Arm on Brimhaven dock.");

		talkToMyArmAtTai = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2781, 3123, 0), "Talk to My Arm east of the Tai Bwo Wannai general store.");

		talkToMurcaily = new NpcStep(this, NpcID.TBWCU_MURCAILY, new WorldPoint(2815, 3083, 0), "Talk to Murcaily in east Tai Bwo Wannai.");
		talkToMurcaily.addDialogSteps("A troll called My Arm wants a favour...", "I was asking you about goutweed...", "I was asking you about hardy goutweed...");
		talkToMyArmAfterMurcaily = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2781, 3123, 0), "Talk to My Arm again east of the Tai Bwo Wannai general store.");

		enterStrongholdForFight = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_DOOR, new WorldPoint(2839, 3690, 0),
			"Return to My Arm on the roof. Be prepared to fight a baby and giant Roc.", combatGear, rake, superCompost, hardyGout, dibber, spade);
		goUpToRoofForFight = new ObjectStep(this, ObjectID.MYARM_LADDER, new WorldPoint(2831, 10077, 2), "Climb back up to My Arm.");
		goUpFromF1ForFight = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRS, new WorldPoint(2843, 10052, 1), "Go up to the roof of the Stronghold and talk to My Arm.");
		goUpFromPrisonForFight = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRS, new WorldPoint(2853, 10107, 0), "Return to My Arm, ready to fight.");
		talkToMyArmForFight = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Talk to My Arm on the roof of the Troll Stronghold.");
		talkToMyArmForFight.addSubSteps(enterStrongholdForFight, goUpToRoofForFight, goUpFromF1ForFight, goUpFromPrisonForFight);

		giveRake = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Give My Arm a rake.", rakeHighlight);
		giveRake.addIcon(ItemID.RAKE);
		pickUpRakeHead = new ItemStep(this, "Pick up the rake head and repair the rake.", rakeHead);
		repairRake = new DetailedQuestStep(this, "Repair the rake.", rakeHead, rakeHandle);
		giveRake.addSubSteps(pickUpRakeHead, repairRake);
		giveSupercompost = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Give My Arm some supercompost.", superCompostHighlight);
		giveSupercompost.addIcon(ItemID.BUCKET_SUPERCOMPOST);
		giveHardyGout = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Give My Arm some hardy gout tubers.", hardyGoutHighlight);
		giveHardyGout.addIcon(ItemID.MYARM_HARDYTUBERS);
		giveDibber = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Give My Arm a seed dibber.", dibberHighlight);
		giveDibber.addIcon(ItemID.DIBBER);
		giveCure = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Give My Arm some plant cure.", plantCureHighlight);
		giveCure.addIcon(ItemID.PLANT_CURE);

		talkToMyArmAfterGrow = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0),
			"Talk to My Arm. Be prepared to fight a baby and giant Roc.");
		killBabyRoc = new NpcStep(this, NpcID.MYARM_BABY_ROC, "Kill the Baby Roc.");
		talkToMyArmAfterBaby = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0),
			"Talk to My Arm. Be prepared to fight the Giant Roc.", combatGear, spade);

		killGiantRoc = new NpcStep(this, NpcID.MYARM_GIANT_ROC, "Kill the Giant Roc. Use protected from ranged, and keep your distance. You can dodge the boulders it throws.");
		talkToMyArmAfterHarvest = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Talk to My Arm.");
		giveSpade = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Give My Arm a spade.", spadeHighlight);
		giveSpade.addIcon(ItemID.SPADE);
		goDownFromMyArmToBurntmeat = new ObjectStep(this, ObjectID.MYARM_EXIT, new WorldPoint(2831, 3677, 0), "Go talk to Burntmeat.");

		goDownToBurntmeat = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRSTOP, new WorldPoint(2844, 10052, 2), "Go talk to Burntmeat.");

		talkToBurntmeatAgain = new NpcStep(this, NpcID.EADGAR_TROLL_CHIEF_COOK, new WorldPoint(2845, 10057, 1),
			"Talk to Burntmeat in the Troll Stronghold.");
		talkToBurntmeatAgain.addSubSteps(goDownFromMyArmToBurntmeat, goDownToBurntmeat);

		enterStrongholdFinish = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_DOOR, new WorldPoint(2839, 3690, 0),
			"Talk to My Arm to finish the quest.");
		goUpToMyArmFinish = new ObjectStep(this, ObjectID.MYARM_LADDER, new WorldPoint(2831, 10077, 2), "Climb back up to My Arm.");
		goUpFromBurntmeatFinish = new ObjectStep(this, ObjectID.TROLL_STRONGHOLD_STAIRS, new WorldPoint(2843, 10052, 1), "Go up to the roof of the Stronghold and talk to My Arm.");

		talkToMyArmFinish = new NpcStep(this, NpcID.MYARM_FIXED, new WorldPoint(2829, 3695, 0), "Talk to My Arm to finish the quest.");
		talkToMyArmFinish.addSubSteps(goUpToMyArmFinish, goUpFromBurntmeatFinish, enterStrongholdFinish);

	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return Arrays.asList(climbingBoots, ugthanki3, superCompost8, rake, dibber, spade, bucket);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return Arrays.asList(combatGear, food, prayerPotions, gamesNecklace);
	}

	@Override
	public List<String> getCombatRequirements()
	{
		ArrayList<String> reqs = new ArrayList<>();
		reqs.add("Baby Roc (level 75)");
		reqs.add("Giant Roc (level 172)");
		return reqs;
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new QuestRequirement(QuestHelperQuest.EADGARS_RUSE, QuestState.FINISHED));
		req.add(new QuestRequirement(QuestHelperQuest.THE_FEUD, QuestState.FINISHED));
		req.add(new QuestRequirement(QuestHelperQuest.JUNGLE_POTION, QuestState.FINISHED));
		req.add(new SkillRequirement(Skill.WOODCUTTING, 10));
		req.add(new SkillRequirement(Skill.FARMING, 29, true));
		// 907 is the Varbit for tai bwo wannai cleanup favour
		req.add(new VarbitRequirement(VarbitID.FAVOUR_PERCENTAGE, Operation.GREATER_EQUAL, 60, "At least 60% favor in the Tai Bwo Wannai Cleanup minigame", false));
		return req;
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(1);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return Arrays.asList(
				new ExperienceReward(Skill.HERBLORE, 10000),
				new ExperienceReward(Skill.FARMING, 5000));
	}

	@Override
	public List<UnlockReward> getUnlockRewards()
	{
		return Collections.singletonList(new UnlockReward("Access to a disease-free herb patch on top of the Troll Stronghold."));
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Starting off", Arrays.asList(talkToBurntmeat, talkToMyArm),
			Collections.singletonList(climbingBoots),
			Collections.singletonList(gamesNecklace)));
		allSteps.add(new PanelDetails("Preparing to grow", Arrays.asList(useBucketOnPot, talkToArmWithLump,
			talkToMyArmUpstairs, readBook, talkToMyArmAfterReading, useUgthankiDung, useCompost, talkToMyArmAfterFertilising),
			Arrays.asList(climbingBoots, bucket, spade, supercompost7, ugthanki3),
			Collections.singletonList(gamesNecklace)));
		allSteps.add(new PanelDetails("Karamja adventure", Arrays.asList(talkToBarnaby, talkAfterBoat, talkToMyArmAtTai, talkToMurcaily, talkToMyArmAfterMurcaily)));
		allSteps.add(new PanelDetails("Troll farming", Arrays.asList(talkToMyArmForFight, giveRake, giveSupercompost, giveHardyGout,
			giveDibber, talkToMyArmAfterGrow, killBabyRoc, killGiantRoc, giveSpade, talkToMyArmAfterHarvest, talkToBurntmeatAgain,
			talkToMyArmFinish), combatGear, rake, superCompost, dibber, spade, hardyGout));
		return allSteps;
	}
}
