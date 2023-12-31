/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.role;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.app.role.IRoleManager;
import android.os.Build;
import android.os.RemoteCallback;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import com.android.modules.utils.BasicShellCommandHandler;
import com.android.permission.compat.UserHandleCompat;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiresApi(Build.VERSION_CODES.S)
class RoleShellCommand extends BasicShellCommandHandler {
    private static final String ROLE_HOLDER_SEPARATOR = ";";

    @NonNull
    private final IRoleManager mRoleManager;

    RoleShellCommand(@NonNull IRoleManager roleManager) {
        mRoleManager = roleManager;
    }

    private class CallbackFuture extends CompletableFuture<Void> {
        @NonNull
        public RemoteCallback createCallback() {
            return new RemoteCallback(result -> {
                boolean successful = result != null;
                if (successful) {
                    complete(null);
                } else {
                    completeExceptionally(new RuntimeException("Failed"));
                }
            });
        }

        public int waitForResult() {
            try {
                get(5, TimeUnit.SECONDS);
                return 0;
            } catch (Exception e) {
                getErrPrintWriter().println("Error: see logcat for details.\n" + e);
                return -1;
            }
        }
    }

    @Override
    public int onCommand(@Nullable String cmd) {
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }

        PrintWriter pw = getOutPrintWriter();
        try {
            switch (cmd) {
                case "get-role-holders":
                    return runGetRoleHolders();
                case "add-role-holder":
                    return runAddRoleHolder();
                case "remove-role-holder":
                    return runRemoveRoleHolder();
                case "clear-role-holders":
                    return runClearRoleHolders();
                case "set-bypassing-role-qualification":
                    return runSetBypassingRoleQualification();
                default:
                    return handleDefaultCommands(cmd);
            }
        } catch (RemoteException e) {
            pw.println("Remote exception: " + e);
        }
        return -1;
    }

    private int getUserIdMaybe() {
        int userId = UserHandleCompat.USER_SYSTEM;
        String option = getNextOption();
        if (option != null && option.equals("--user")) {
            userId = Integer.parseInt(getNextArgRequired());
        }
        return userId;
    }

    private int getFlagsMaybe() {
        String flags = getNextArg();
        if (flags == null) {
            return 0;
        }
        return Integer.parseInt(flags);
    }

    private int runGetRoleHolders() throws RemoteException {
        int userId = getUserIdMaybe();
        String roleName = getNextArgRequired();

        List<String> roleHolders = mRoleManager.getRoleHoldersAsUser(roleName, userId);
        getOutPrintWriter().println(String.join(ROLE_HOLDER_SEPARATOR, roleHolders));
        return 0;
    }

    private int runAddRoleHolder() throws RemoteException {
        int userId = getUserIdMaybe();
        String roleName = getNextArgRequired();
        String packageName = getNextArgRequired();
        int flags = getFlagsMaybe();

        CallbackFuture future = new CallbackFuture();
        mRoleManager.addRoleHolderAsUser(roleName, packageName, flags, userId,
                future.createCallback());
        return future.waitForResult();
    }

    private int runRemoveRoleHolder() throws RemoteException {
        int userId = getUserIdMaybe();
        String roleName = getNextArgRequired();
        String packageName = getNextArgRequired();
        int flags = getFlagsMaybe();

        CallbackFuture future = new CallbackFuture();
        mRoleManager.removeRoleHolderAsUser(roleName, packageName, flags, userId,
                future.createCallback());
        return future.waitForResult();
    }

    private int runClearRoleHolders() throws RemoteException {
        int userId = getUserIdMaybe();
        String roleName = getNextArgRequired();
        int flags = getFlagsMaybe();

        CallbackFuture future = new CallbackFuture();
        mRoleManager.clearRoleHoldersAsUser(roleName, flags, userId, future.createCallback());
        return future.waitForResult();
    }

    private int runSetBypassingRoleQualification() throws RemoteException {
        Boolean value = Boolean.parseBoolean(getNextArgRequired());
        mRoleManager.setBypassingRoleQualification(value);
        return 0;
    }

    @Override
    public void onHelp() {
        PrintWriter pw = getOutPrintWriter();
        pw.println("Role (role) commands:");
        pw.println("  help or -h");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  get-role-holders [--user USER_ID] ROLE");
        pw.println("  add-role-holder [--user USER_ID] ROLE PACKAGE [FLAGS]");
        pw.println("  remove-role-holder [--user USER_ID] ROLE PACKAGE [FLAGS]");
        pw.println("  clear-role-holders [--user USER_ID] ROLE [FLAGS]");
        pw.println("  set-bypassing-role-qualification true|false");
        pw.println();
    }
}
