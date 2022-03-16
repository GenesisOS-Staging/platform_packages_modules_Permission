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

package android.safetycenter.config.cts

import android.content.res.Resources
import android.os.Build.VERSION_CODES.TIRAMISU
import android.safetycenter.config.SafetySourcesGroup
import android.safetycenter.testing.AnyTester
import android.safetycenter.testing.ParcelableTester
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/** CTS tests for [SafetySourcesGroup]. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = TIRAMISU, codeName = "Tiramisu")
class SafetySourcesGroupTest {
    @Test
    fun getType_returnsType() {
        assertThat(COLLAPSIBLE_WITH_SUMMARY.type)
            .isEqualTo(SafetySourcesGroup.SAFETY_SOURCES_GROUP_TYPE_COLLAPSIBLE)
        assertThat(COLLAPSIBLE_WITH_ICON.type)
            .isEqualTo(SafetySourcesGroup.SAFETY_SOURCES_GROUP_TYPE_COLLAPSIBLE)
        assertThat(COLLAPSIBLE_WITH_BOTH.type)
            .isEqualTo(SafetySourcesGroup.SAFETY_SOURCES_GROUP_TYPE_COLLAPSIBLE)
        assertThat(RIGID.type).isEqualTo(SafetySourcesGroup.SAFETY_SOURCES_GROUP_TYPE_RIGID)
        assertThat(HIDDEN.type).isEqualTo(SafetySourcesGroup.SAFETY_SOURCES_GROUP_TYPE_HIDDEN)
    }

    @Test
    fun getId_returnsId() {
        assertThat(COLLAPSIBLE_WITH_SUMMARY.id).isEqualTo(COLLAPSIBLE_WITH_SUMMARY_ID)
        assertThat(COLLAPSIBLE_WITH_ICON.id).isEqualTo(COLLAPSIBLE_WITH_ICON_ID)
        assertThat(COLLAPSIBLE_WITH_BOTH.id).isEqualTo(COLLAPSIBLE_WITH_BOTH_ID)
        assertThat(RIGID.id).isEqualTo(RIGID_ID)
        assertThat(HIDDEN.id).isEqualTo(HIDDEN_ID)
    }

    @Test
    fun getTitleResId_returnsTitleResId() {
        assertThat(COLLAPSIBLE_WITH_SUMMARY.titleResId).isEqualTo(REFERENCE_RES_ID)
        assertThat(COLLAPSIBLE_WITH_ICON.titleResId).isEqualTo(REFERENCE_RES_ID)
        assertThat(COLLAPSIBLE_WITH_BOTH.titleResId).isEqualTo(REFERENCE_RES_ID)
        assertThat(RIGID.titleResId).isEqualTo(REFERENCE_RES_ID)
        // This is not an enforced invariant, titleResId should just be ignored for hidden groups
        assertThat(HIDDEN.titleResId).isEqualTo(Resources.ID_NULL)
    }

    @Test
    fun getSummaryResId_returnsSummaryResId() {
        assertThat(COLLAPSIBLE_WITH_SUMMARY.summaryResId).isEqualTo(REFERENCE_RES_ID)
        assertThat(COLLAPSIBLE_WITH_ICON.summaryResId).isEqualTo(Resources.ID_NULL)
        assertThat(COLLAPSIBLE_WITH_BOTH.summaryResId).isEqualTo(REFERENCE_RES_ID)
        assertThat(RIGID.summaryResId).isEqualTo(Resources.ID_NULL)
        // This is not an enforced invariant, summaryResId should just be ignored for hidden groups
        assertThat(HIDDEN.summaryResId).isEqualTo(Resources.ID_NULL)
    }

    @Test
    fun getStatelessIconType_returnsStatelessIconType() {
        assertThat(COLLAPSIBLE_WITH_SUMMARY.statelessIconType)
            .isEqualTo(SafetySourcesGroup.STATELESS_ICON_TYPE_NONE)
        assertThat(COLLAPSIBLE_WITH_ICON.statelessIconType)
            .isEqualTo(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
        assertThat(COLLAPSIBLE_WITH_BOTH.statelessIconType)
            .isEqualTo(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
        assertThat(RIGID.statelessIconType).isEqualTo(SafetySourcesGroup.STATELESS_ICON_TYPE_NONE)
        // This is not an enforced invariant
        // statelessIconType should just be ignored for hidden groups
        assertThat(HIDDEN.statelessIconType).isEqualTo(SafetySourcesGroup.STATELESS_ICON_TYPE_NONE)
    }

    @Test
    fun getSafetySources_returnsSafetySources() {
        assertThat(COLLAPSIBLE_WITH_SUMMARY.safetySources)
            .containsExactly(SafetySourceTest.DYNAMIC_BAREBONE)
        assertThat(COLLAPSIBLE_WITH_ICON.safetySources)
            .containsExactly(SafetySourceTest.STATIC_BAREBONE)
        assertThat(COLLAPSIBLE_WITH_BOTH.safetySources)
            .containsExactly(
                SafetySourceTest.DYNAMIC_BAREBONE,
                SafetySourceTest.STATIC_BAREBONE,
                SafetySourceTest.ISSUE_ONLY_BAREBONE
            ).inOrder()
        assertThat(RIGID.safetySources).containsExactly(SafetySourceTest.STATIC_BAREBONE)
        assertThat(HIDDEN.safetySources).containsExactly(SafetySourceTest.ISSUE_ONLY_BAREBONE)
    }

    @Test
    fun describeContents_returns0() {
        assertThat(COLLAPSIBLE_WITH_SUMMARY.describeContents()).isEqualTo(0)
        assertThat(COLLAPSIBLE_WITH_ICON.describeContents()).isEqualTo(0)
        assertThat(COLLAPSIBLE_WITH_BOTH.describeContents()).isEqualTo(0)
        assertThat(RIGID.describeContents()).isEqualTo(0)
        assertThat(HIDDEN.describeContents()).isEqualTo(0)
    }

    @Test
    fun createFromParcel_withWriteToParcel_returnsOriginalSafetySourcesGroup() {
        ParcelableTester.assertThatRoundTripReturnsOriginal(
            COLLAPSIBLE_WITH_SUMMARY,
            SafetySourcesGroup.CREATOR
        )
        ParcelableTester.assertThatRoundTripReturnsOriginal(
            COLLAPSIBLE_WITH_ICON,
            SafetySourcesGroup.CREATOR
        )
        ParcelableTester.assertThatRoundTripReturnsOriginal(
            COLLAPSIBLE_WITH_BOTH,
            SafetySourcesGroup.CREATOR
        )
        ParcelableTester.assertThatRoundTripReturnsOriginal(RIGID, SafetySourcesGroup.CREATOR)
        ParcelableTester.assertThatRoundTripReturnsOriginal(HIDDEN, SafetySourcesGroup.CREATOR)
    }

    // TODO(b/208473675): Use `EqualsTester` for testing `hashcode` and `equals`.
    @Test
    fun hashCode_equals_toString_withEqualByReference_areEqual() {
        AnyTester.assertThatRepresentationsAreEqual(
            COLLAPSIBLE_WITH_SUMMARY,
            COLLAPSIBLE_WITH_SUMMARY
        )
        AnyTester.assertThatRepresentationsAreEqual(COLLAPSIBLE_WITH_ICON, COLLAPSIBLE_WITH_ICON)
        AnyTester.assertThatRepresentationsAreEqual(COLLAPSIBLE_WITH_BOTH, COLLAPSIBLE_WITH_BOTH)
        AnyTester.assertThatRepresentationsAreEqual(RIGID, RIGID)
        AnyTester.assertThatRepresentationsAreEqual(HIDDEN, HIDDEN)
    }

    @Test
    fun hashCode_equals_toString_withAllFieldsEqual_areEqual() {
        val collapsibleWithBothCopy = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_BOTH_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .setSummaryResId(REFERENCE_RES_ID)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
            .addSafetySource(SafetySourceTest.DYNAMIC_BAREBONE)
            .addSafetySource(SafetySourceTest.STATIC_BAREBONE)
            .addSafetySource(SafetySourceTest.ISSUE_ONLY_BAREBONE)
            .build()
        AnyTester.assertThatRepresentationsAreEqual(COLLAPSIBLE_WITH_BOTH, collapsibleWithBothCopy)
    }

    @Test
    fun hashCode_equals_toString_withDifferentTypes_areNotEqual() {
        AnyTester.assertThatRepresentationsAreNotEqual(COLLAPSIBLE_WITH_BOTH, RIGID)
        AnyTester.assertThatRepresentationsAreNotEqual(RIGID, HIDDEN)
        AnyTester.assertThatRepresentationsAreNotEqual(HIDDEN, COLLAPSIBLE_WITH_BOTH)
    }

    @Test
    fun hashCode_equals_toString_withDifferentIds_areNotEqual() {
        val collapsibleWithBothAlt = SafetySourcesGroup.Builder()
            .setId("other")
            .setTitleResId(REFERENCE_RES_ID)
            .setSummaryResId(REFERENCE_RES_ID)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
            .addSafetySource(SafetySourceTest.DYNAMIC_BAREBONE)
            .build()
        AnyTester.assertThatRepresentationsAreNotEqual(
            COLLAPSIBLE_WITH_BOTH,
            collapsibleWithBothAlt
        )
    }

    @Test
    fun hashCode_equals_toString_withDifferentTitleResIds_areNotEqual() {
        val collapsibleWithBothAlt = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_BOTH_ID)
            .setTitleResId(-1)
            .setSummaryResId(REFERENCE_RES_ID)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
            .addSafetySource(SafetySourceTest.DYNAMIC_BAREBONE)
            .build()
        AnyTester.assertThatRepresentationsAreNotEqual(
            COLLAPSIBLE_WITH_BOTH,
            collapsibleWithBothAlt
        )
    }

    @Test
    fun hashCode_equals_toString_withDifferentSummaryResIds_areNotEqual() {
        val collapsibleWithBothAlt = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_BOTH_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .setSummaryResId(-1)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
            .addSafetySource(SafetySourceTest.DYNAMIC_BAREBONE)
            .build()
        AnyTester.assertThatRepresentationsAreNotEqual(
            COLLAPSIBLE_WITH_BOTH,
            collapsibleWithBothAlt
        )
    }

    @Test
    fun hashCode_equals_toString_withDifferentInitialDisplayStates_areNotEqual() {
        val collapsibleWithBothAlt = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_BOTH_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .setSummaryResId(REFERENCE_RES_ID)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_NONE)
            .addSafetySource(SafetySourceTest.DYNAMIC_BAREBONE)
            .build()
        AnyTester.assertThatRepresentationsAreNotEqual(
            COLLAPSIBLE_WITH_BOTH,
            collapsibleWithBothAlt
        )
    }

    @Test
    fun hashCode_equals_toString_withDifferentSafetySources_areNotEqual() {
        val collapsibleWithBothAlt = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_BOTH_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .setSummaryResId(REFERENCE_RES_ID)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
            .addSafetySource(SafetySourceTest.STATIC_BAREBONE)
            .build()
        AnyTester.assertThatRepresentationsAreNotEqual(
            COLLAPSIBLE_WITH_BOTH,
            collapsibleWithBothAlt
        )
    }

    companion object {
        private const val REFERENCE_RES_ID = 9999

        private const val COLLAPSIBLE_WITH_SUMMARY_ID = "collapsible_with_summary"
        private const val COLLAPSIBLE_WITH_ICON_ID = "collapsible_with_icon"
        private const val COLLAPSIBLE_WITH_BOTH_ID = "collapsible_with_both"
        private const val RIGID_ID = "rigid"
        private const val HIDDEN_ID = "hidden"

        private val COLLAPSIBLE_WITH_SUMMARY = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_SUMMARY_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .setSummaryResId(REFERENCE_RES_ID)
            .addSafetySource(SafetySourceTest.DYNAMIC_BAREBONE)
            .build()

        private val COLLAPSIBLE_WITH_ICON = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_ICON_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
            .addSafetySource(SafetySourceTest.STATIC_BAREBONE)
            .build()

        private val COLLAPSIBLE_WITH_BOTH = SafetySourcesGroup.Builder()
            .setId(COLLAPSIBLE_WITH_BOTH_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .setSummaryResId(REFERENCE_RES_ID)
            .setStatelessIconType(SafetySourcesGroup.STATELESS_ICON_TYPE_PRIVACY)
            .addSafetySource(SafetySourceTest.DYNAMIC_BAREBONE)
            .addSafetySource(SafetySourceTest.STATIC_BAREBONE)
            .addSafetySource(SafetySourceTest.ISSUE_ONLY_BAREBONE)
            .build()

        internal val RIGID = SafetySourcesGroup.Builder()
            .setId(RIGID_ID)
            .setTitleResId(REFERENCE_RES_ID)
            .addSafetySource(SafetySourceTest.STATIC_BAREBONE)
            .build()

        internal val HIDDEN = SafetySourcesGroup.Builder()
            .setId(HIDDEN_ID)
            .addSafetySource(SafetySourceTest.ISSUE_ONLY_BAREBONE)
            .build()
    }
}
