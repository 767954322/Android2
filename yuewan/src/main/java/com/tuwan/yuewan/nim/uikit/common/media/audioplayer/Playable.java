package com.tuwan.yuewan.nim.uikit.common.media.audioplayer;

public interface Playable {
	long getDuration();
	String getPath();
	boolean isAudioEqual(Playable audio);
}