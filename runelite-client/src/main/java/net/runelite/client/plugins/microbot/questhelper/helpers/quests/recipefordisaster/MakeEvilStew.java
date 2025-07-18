/*
 * Copyright (c) 2021, Zoinkwiz
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
package net.runelite.client.plugins.microbot.questhelper.helpers.quests.recipefordisaster;

import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.collections.NpcCollections;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.QuestHelper;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.FollowerItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.eventbus.Subscribe;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MakeEvilStew extends DetailedOwnerStep
{

	DetailedQuestStep catchRats, enterBasement, useStewOnEvilDave, restart;

	Requirement inEvilDaveRoom;

	Zone evilDaveRoom;

	ItemRequirement redSpice, orangeSpice, brownSpice, yellowSpice, evilStewHighlighted, evilStew, stew;

	Requirement cat;

	public MakeEvilStew(QuestHelper questHelper)
	{
		super(questHelper);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		updateSteps();
	}

	@Override
	protected void updateSteps()
	{
		int redNeeded = client.getVarbitValue(1883);
		int yellowNeeded = client.getVarbitValue(1884);
		int brownNeeded = client.getVarbitValue(1885);
		int orangeNeeded = client.getVarbitValue(1886);

		int redInStew = client.getVarbitValue(Varbits.SPICY_STEW_RED_SPICES);
		int yellowInStew = client.getVarbitValue(Varbits.SPICY_STEW_YELLOW_SPICES);
		int brownInStew = client.getVarbitValue(Varbits.SPICY_STEW_BROWN_SPICES);
		int orangeInStew = client.getVarbitValue(Varbits.SPICY_STEW_ORANGE_SPICES);

		int numRedStillNeeded = redNeeded - redInStew;
		int numOrangeStillNeeded = orangeNeeded - orangeInStew;
		int numBrownStillNeeded = brownNeeded - brownInStew;
		int numYellowStillNeeded = yellowNeeded - yellowInStew;

		if (!inEvilDaveRoom.check(client))
		{
			startUpStep(enterBasement);
		}

		catchRats.setRequirements(Collections.singletonList(cat));
		catchRats.setText("Have your cat catch Hell-Rats for spices, and add them " +
			"to your stew. You will need to add a random number between 1-3 of each spice (red/orange/yellow/brown). " +
			"Try adding 1 of a spice to a stew, then using the stew on Evil Dave to see if it's right. If not, try " +
			"with 2 and then 3. " +
			"Rinse and repeat until you know the right quantity of each spice, then use the perfect stew combination " +
			"on Evil Dave.");

		startUpStep(catchRats);

	}

	@Override
	protected void setupSteps()
	{
		setupRequirements();
		setupZones();
		setupConditions();

		catchRats = new DetailedQuestStep(getQuestHelper(), "Have your cat catch Hell-Rats for spices, and add them " +
			"to your stew to match the required quantities.");

		restart = new DetailedQuestStep(getQuestHelper(), "You've added too much of a spice. Eat your spicy stew and " +
			"start again.", evilStewHighlighted);

		enterBasement = new ObjectStep(getQuestHelper(), ObjectID._100_DAVE_CELLER_TRAPDOOR_CLOSED, new WorldPoint(3077, 3493, 0),
			"Go back down to Evil Dave.");
		((ObjectStep) enterBasement).addAlternateObjects(ObjectID._100_DAVE_CELLER_TRAPDOOR_OPEN);

		useStewOnEvilDave = new NpcStep(getQuestHelper(), NpcID.HUNDRED_EVIL_DAVE, new WorldPoint(3080, 9889, 0),
			"Use the spicy stew on Evil Dave.", evilStewHighlighted);
	}

	protected void setupRequirements()
	{
		redSpice = new ItemRequirement("Red spice", ItemID.HUNDRED_DAVE_SPICE_RED_1);
		redSpice.addAlternates(ItemID.HUNDRED_DAVE_SPICE_RED_2, ItemID.HUNDRED_DAVE_SPICE_RED_3, ItemID.HUNDRED_DAVE_SPICE_RED_4);
		redSpice.setHighlightInInventory(true);

		orangeSpice = new ItemRequirement("Orange spice", ItemID.HUNDRED_DAVE_SPICE_ORANGE_1);
		orangeSpice.addAlternates(ItemID.HUNDRED_DAVE_SPICE_ORANGE_2, ItemID.HUNDRED_DAVE_SPICE_ORANGE_3, ItemID.HUNDRED_DAVE_SPICE_ORANGE_4);
		orangeSpice.setHighlightInInventory(true);

		yellowSpice = new ItemRequirement("Yellow spice", ItemID.HUNDRED_DAVE_SPICE_YELLOW_1);
		yellowSpice.addAlternates(ItemID.HUNDRED_DAVE_SPICE_YELLOW_2, ItemID.HUNDRED_DAVE_SPICE_YELLOW_3, ItemID.HUNDRED_DAVE_SPICE_YELLOW_4);
		yellowSpice.setHighlightInInventory(true);

		brownSpice = new ItemRequirement("Brown spice", ItemID.HUNDRED_DAVE_SPICE_BROWN_1);
		brownSpice.addAlternates(ItemID.HUNDRED_DAVE_SPICE_BROWN_2, ItemID.HUNDRED_DAVE_SPICE_BROWN_3, ItemID.HUNDRED_DAVE_SPICE_BROWN_4);
		brownSpice.setHighlightInInventory(true);

		evilStew = new ItemRequirement("Spicy stew", ItemID.HUNDRED_DAVE_STEW);
		evilStewHighlighted = new ItemRequirement("Spicy stew", ItemID.HUNDRED_DAVE_STEW);
		evilStewHighlighted.setHighlightInInventory(true);

		stew = new ItemRequirement("Stew", ItemID.STEW);
		stew.setHighlightInInventory(true);

		cat = new FollowerItemRequirement("A non-overgrown cat for catching rats",
			ItemCollections.HUNTING_CATS,
			NpcCollections.getHuntingCats());
	}

	public void setupZones()
	{
		evilDaveRoom = new Zone(new WorldPoint(3068, 9874, 0), new WorldPoint(3086, 9904, 0));
	}

	public void setupConditions()
	{
		inEvilDaveRoom = new ZoneRequirement(evilDaveRoom);
	}

	@Override
	public Collection<QuestStep> getSteps()
	{
		return Arrays.asList(catchRats, enterBasement, useStewOnEvilDave);
	}

	public List<QuestStep> getDisplaySteps()
	{
		return Arrays.asList(catchRats, useStewOnEvilDave);
	}
}
