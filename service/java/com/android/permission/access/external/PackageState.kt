/*
 * Copyright (C) 2021 The Android Open Source Project
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

package com.android.permission.access.external

import android.util.SparseArray

interface PackageState {
    val androidPackage: AndroidPackage?
    val appId: Int
    val isSystem: Boolean
    val packageName: String
    val userStates: SparseArray<PackageUserState>
}

interface AndroidPackage {
    val packageName: String
    val appId: Int
    val adoptPermissions: List<String>
    val permissions: List<ParsedPermission>
    val permissionGroups: List<ParsedPermissionGroup>
    val requestedPermissions: List<String>
}

interface ParsedPermission {
    val name: String
    val isTree: Boolean
    val packageName: String
}

interface ParsedPermissionGroup {
    val name: String
    val packageName: String
}

interface PackageUserState {
    val isInstantApp: Boolean
}