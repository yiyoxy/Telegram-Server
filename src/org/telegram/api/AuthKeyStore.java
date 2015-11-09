/*
 *     This file is part of Telegram Server
 *     Copyright (C) 2015  Aykut Alparslan KOÇ
 *
 *     Telegram Server is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Telegram Server is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.telegram.api;

import org.telegram.data.DatabaseConnection;
import org.telegram.data.HazelcastConnection;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by aykut on 06/11/15.
 */
public class AuthKeyStore {

    //private HashMap<Long, byte[]> authKeys = new HashMap<>();
    private ConcurrentMap<Long, byte[]> authKeysShared = HazelcastConnection.getInstance().getMap("telegram_auth_keys");

    private static AuthKeyStore _instance;

    private AuthKeyStore() {

    }

    public static AuthKeyStore getInstance() {
        if (_instance == null) {
            _instance = new AuthKeyStore();
        }
        return _instance;
    }

    public byte[] getAuthKey(long authKeyId) {
        byte[] authKey = authKeysShared.get(authKeyId);
        if (authKey == null || authKey.length == 0) {
            authKey = DatabaseConnection.getInstance().getAuthKey(authKeyId);
            authKeysShared.put(authKeyId, authKey);
        }
        return authKey;
    }
}