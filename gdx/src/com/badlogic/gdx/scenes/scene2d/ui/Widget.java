/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

/** An {@link Actor} that participates in layout and provides a minimum, preferred, and maximum size.
 * <p>
 * The default preferred size of a widget is 0 and this is almost always overridden by a subclass. The default minimum size
 * returns the preferred size, so a subclass may choose to return 0 if it wants to allow itself to be sized smaller. The default
 * maximum size is 0, which means no maximum size.
 * <p>
 * See {@link Layout} for details on how a widget should participate in layout. A widget's mutator methods should call
 * {@link #invalidate()} or {@link #invalidateHierarchy()} as needed.
 * @author mzechner
 * @author Nathan Sweet */
public class Widget extends Actor implements Layout {
	private boolean needsLayout = true;
	private boolean fillParent;
	private boolean layoutEnabled = true;

	@Override
	public float getMinWidth () {
		return getPrefWidth();
	}

	@Override
	public float getMinHeight () {
		return getPrefHeight();
	}

	@Override
	public float getPrefWidth () {
		return 0;
	}

	@Override
	public float getPrefHeight () {
		return 0;
	}

	@Override
	public float getMaxWidth () {
		return 0;
	}

	@Override
	public float getMaxHeight () {
		return 0;
	}

	@Override
	public void setLayoutEnabled (boolean enabled) {
		layoutEnabled = enabled;
		if (enabled) invalidateHierarchy();
	}

	@Override
	public void validate () {
		if (!layoutEnabled) return;

		Group parent = getParent();
		if (fillParent && parent != null) {
			float parentWidth, parentHeight;
			Stage stage = getStage();
			if (stage != null && parent == stage.getRoot()) {
				parentWidth = stage.getWidth();
				parentHeight = stage.getHeight();
			} else {
				parentWidth = parent.getWidth();
				parentHeight = parent.getHeight();
			}
			setSize(parentWidth, parentHeight);
		}

		if (!needsLayout) return;
		needsLayout = false;
		layout();
	}

	/** Returns true if the widget's layout has been {@link #invalidate() invalidated}. */
	public boolean needsLayout () {
		return needsLayout;
	}

	@Override
	public void invalidate () {
		needsLayout = true;
	}

	@Override
	public void invalidateHierarchy () {
		if (!layoutEnabled) return;
		invalidate();
		Group parent = getParent();
		if (parent instanceof Layout) ((Layout)parent).invalidateHierarchy();
	}

	@Override
	protected void sizeChanged () {
		invalidate();
	}

	@Override
	public void pack () {
		setSize(getPrefWidth(), getPrefHeight());
		validate();
	}

	@Override
	public void setFillParent (boolean fillParent) {
		this.fillParent = fillParent;
	}

	/** If this method is overridden, the super method or {@link #validate()} should be called to ensure the widget is laid out. */
	@Override
	public void draw (Batch batch, float parentAlpha) {
		validate();
	}

	@Override
	public void layout () {
	}
}
