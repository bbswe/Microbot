/*
 * Copyright (c) 2023, jLereback <https://github.com/jLereback>
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
package net.runelite.client.plugins.microbot.questhelper.helpers.skills.agility;

import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.QuestHelper;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.steps.ConditionalStep;
import net.runelite.client.plugins.microbot.questhelper.steps.DetailedQuestStep;
import net.runelite.client.plugins.microbot.questhelper.steps.ObjectStep;
import net.runelite.client.plugins.microbot.questhelper.steps.QuestStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ObjectID;

import java.util.Arrays;
import java.util.Collections;

public class DraynorVillage extends AgilityCourse
{
	QuestStep draynorSidebar;
	QuestStep climbRoughWall, walkFirstRope, walkSecondRope, balanceWall, jumpUpWall, jumpGap, climbDownCrate;
	Zone firstRopeZone, secondRopeZone, narrowWallZone, wallZone, gapZone, crateZone;
	ZoneRequirement inFirstRopeZone, inSecondRopeZone, inNarrowWallZone, inWallZone, inGapZone, inCrateZone;

	ConditionalStep draynorStep;
	PanelDetails draynorPanels;

	public DraynorVillage(QuestHelper questHelper)
	{
		super(questHelper);
	}

	@Override
	protected ConditionalStep loadStep()
	{
		setupZones();
		setupConditions();
		setupSteps();
		addSteps();

		return draynorStep;
	}

	@Override
	protected void setupConditions()
	{
		inFirstRopeZone = new ZoneRequirement(firstRopeZone);
		inSecondRopeZone = new ZoneRequirement(secondRopeZone);
		inNarrowWallZone = new ZoneRequirement(narrowWallZone);
		inWallZone = new ZoneRequirement(wallZone);
		inGapZone = new ZoneRequirement(gapZone);
		inCrateZone = new ZoneRequirement(crateZone);
	}

	@Override
	protected void setupZones()
	{
		firstRopeZone = new Zone(new WorldPoint(3094, 3277, 3), new WorldPoint(3102, 3281, 3));
		secondRopeZone = new Zone(new WorldPoint(3087, 3272, 3), new WorldPoint(3093, 3278, 2));
		narrowWallZone = new Zone(new WorldPoint(3089, 3262, 3), new WorldPoint(3094, 3271, 3));
		wallZone = new Zone(new WorldPoint(3087, 3256, 3), new WorldPoint(3088, 3262, 3));
		gapZone = new Zone(new WorldPoint(3087, 3252, 3), new WorldPoint(3095, 3255, 3));
		crateZone = new Zone(new WorldPoint(3095, 3256, 3), new WorldPoint(3101, 3261, 3));
	}

	@Override
	protected void setupSteps()
	{
		//Draynor Village obstacles
		climbRoughWall = new ObjectStep(this.questHelper, ObjectID.ROOFTOPS_DRAYNOR_WALLCLIMB, new WorldPoint(3103, 3279, 0),
			"Climb the rough wall just north-west of the gate of the big wheat-field in Draynor Village.",
			Collections.EMPTY_LIST, Arrays.asList(recommendedItems));

		walkFirstRope = new ObjectStep(this.questHelper, ObjectID.ROOFTOPS_DRAYNOR_TIGHTROPE_1, new WorldPoint(3098, 3277, 3),
			"Cross the tightrope.", Collections.EMPTY_LIST, Arrays.asList(recommendedItems));

		walkSecondRope = new ObjectStep(this.questHelper, ObjectID.ROOFTOPS_DRAYNOR_TIGHTROPE_2, new WorldPoint(3092, 3276, 3),
			"Cross the next tightrope.", Collections.EMPTY_LIST, Arrays.asList(recommendedItems));

		balanceWall = new ObjectStep(this.questHelper, ObjectID.ROOFTOPS_DRAYNOR_WALLCROSSING, new WorldPoint(3089, 3264, 3),
			"Balance across thr narrow wall.", Collections.EMPTY_LIST, Arrays.asList(recommendedItems));

		jumpUpWall = new ObjectStep(this.questHelper, ObjectID.ROOFTOPS_DRAYNOR_WALLSCRAMBLE, new WorldPoint(3088, 3256, 3),
			"Jump up the wall.", Collections.EMPTY_LIST, Arrays.asList(recommendedItems));

		jumpGap = new ObjectStep(this.questHelper, ObjectID.ROOFTOPS_DRAYNOR_LEAPDOWN, new WorldPoint(3095, 3255, 3),
			"Climb over the gap.", Collections.EMPTY_LIST, Arrays.asList(recommendedItems));

		climbDownCrate = new ObjectStep(this.questHelper, ObjectID.ROOFTOPS_DRAYNOR_CRATE, new WorldPoint(3102, 3261, 3),
			"Climb down onto the crate.", Collections.EMPTY_LIST, Arrays.asList(recommendedItems));
	}

	@Override
	protected void addSteps()
	{
		draynorStep = new ConditionalStep(this.questHelper, climbRoughWall);
		draynorStep.addStep(inFirstRopeZone, walkFirstRope);
		draynorStep.addStep(inSecondRopeZone, walkSecondRope);
		draynorStep.addStep(inNarrowWallZone, balanceWall);
		draynorStep.addStep(inWallZone, jumpUpWall);
		draynorStep.addStep(inGapZone, jumpGap);
		draynorStep.addStep(inCrateZone, climbDownCrate);

		draynorSidebar = new DetailedQuestStep(this.questHelper, "Train agility at the Draynor Village Rooftop Course" +
			", starting just north-west of the gate of the big wheat-field in Draynor Village.");
		draynorSidebar.addSubSteps(climbRoughWall, walkFirstRope, walkSecondRope, balanceWall, jumpUpWall, jumpGap, climbDownCrate);
	}

	@Override
	protected PanelDetails getPanelDetails()
	{
		draynorPanels = new PanelDetails("1 - 20: Draynor Village", Collections.singletonList(draynorSidebar)
		);
		return draynorPanels;
	}
}
