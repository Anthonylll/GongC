package com.gcapp.tc.sd.ui.win_dialog.widget;

public enum Effectstype {

	Fadein(FadeIn.class), Slideleft(SlideLeft.class), Slidetop(SlideTop.class), SlideBottom(
			SlideBottom.class), Slideright(SlideRight.class), Fall(Fall.class), Newspager(
			NewsPaper.class), Fliph(FlipH.class), Flipv(FlipV.class), RotateBottom(
			RotateBottom.class), RotateLeft(RotateLeft.class), Slit(Slit.class), Shake(
			Shake.class), Sidefill(SideFall.class);
	private Class effectsClazz;

	private Effectstype(Class mclass) {
		effectsClazz = mclass;
	}

	public BaseEffects getAnimator() {
		try {
			return (BaseEffects) effectsClazz.newInstance();
		} catch (Exception e) {
			throw new Error("Can not init animatorClazz instance");
		}
	}
}
