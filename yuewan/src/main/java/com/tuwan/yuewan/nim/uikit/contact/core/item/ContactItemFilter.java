package com.tuwan.yuewan.nim.uikit.contact.core.item;

import java.io.Serializable;

public interface ContactItemFilter extends Serializable {
	boolean filter(AbsContactItem item);
}
