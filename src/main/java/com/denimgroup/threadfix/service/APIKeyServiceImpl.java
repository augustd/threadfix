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
package com.denimgroup.threadfix.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.commons.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denimgroup.threadfix.data.dao.APIKeyDao;
import com.denimgroup.threadfix.data.entities.APIKey;

@Service
@Transactional(readOnly = true)
public class APIKeyServiceImpl implements APIKeyService {
	
	private final Log log = LogFactory.getLog(APIKeyService.class);

	private APIKeyDao apiKeyDao = null;

	@Autowired
	public APIKeyServiceImpl(APIKeyDao apiKeyDao) {
		this.apiKeyDao = apiKeyDao;
	}

	@Override
	public List<APIKey> loadAll() {
		return apiKeyDao.retrieveAll();
	}

	@Override
	public APIKey loadAPIKey(int apiKeyId) {
		return apiKeyDao.retrieveById(apiKeyId);
	}
	
	@Override
	public APIKey loadAPIKey(String key) {		
		return apiKeyDao.retrieveByKey(key);
	}

	@Override
	@Transactional(readOnly = false)
	public void storeAPIKey(APIKey apiKey) {
		apiKeyDao.saveOrUpdate(apiKey);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteById(int apiKeyId) {
		log.info("Deleting API key with id " + apiKeyId);
		
		APIKey apiKey = apiKeyDao.retrieveById(apiKeyId);
		apiKey.setActive(false);
		apiKeyDao.saveOrUpdate(apiKey);
	}

	@Override
	public APIKey createAPIKey(String note, boolean restricted) {
		APIKey key = new APIKey();
		
		String editedNote = note;
		
		if (editedNote != null && editedNote.length() > 255)
			editedNote = editedNote.substring(0, 254);
		
		String keyString = generateNewSecureRandomKey();
		
		if (keyString != null && keyString.length() > 50)
			keyString = keyString.substring(0, 49);
		
		key.setNote(editedNote);
		key.setIsRestrictedKey(restricted);
		key.setApiKey(keyString);
		
		return key;
	}
	
	private String generateNewSecureRandomKey() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

			String newKey = "";
			
			newKey = newKey.concat(Base64.encode(toByteArray(random.nextLong())).trim());
			newKey = newKey.concat(Base64.encode(toByteArray(random.nextLong())).trim());
			newKey = newKey.concat(Base64.encode(toByteArray(random.nextLong())).trim());
			newKey = newKey.concat(Base64.encode(toByteArray(random.nextLong())).trim());
						
			newKey = newKey.replaceAll("[\\[!@#$%\\^&*\\(\\)=\\-+/]", "");
			
			return newKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		
		log.error("API Key Generation failed. Make sure the algorithm is supported.");
		return null;
	}
	
	private byte[] toByteArray(long data) {
		return new byte[] {
				(byte)((data >> 56) & 0xff),
				(byte)((data >> 48) & 0xff),
				(byte)((data >> 40) & 0xff),
				(byte)((data >> 32) & 0xff),
				(byte)((data >> 24) & 0xff),
				(byte)((data >> 16) & 0xff),
				(byte)((data >> 8) & 0xff),
				(byte)((data >> 0) & 0xff),
		};
	}
}
