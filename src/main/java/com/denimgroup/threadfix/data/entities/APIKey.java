////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2011 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 1.1 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is Vulnerability Manager.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "APIKey")
public class APIKey extends AuditableEntity {
	
	private static final long serialVersionUID = 5185330378304148078L;

	private String key;
	private String note;
	
	private boolean isRestrictedKey;

	@Column(length = 50, nullable = false)
	public String getApiKey() {
		return key;
	}

	public void setApiKey(String key) {
		this.key = key;
	}

	@Column(length = 255)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(nullable = false)
	public boolean getIsRestrictedKey() {
		return isRestrictedKey;
	}
	
	public void setIsRestrictedKey(boolean restricted) {
		this.isRestrictedKey = restricted;
	}
}
