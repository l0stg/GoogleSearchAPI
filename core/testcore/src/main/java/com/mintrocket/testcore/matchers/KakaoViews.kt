package com.mintrocket.testcore.matchers

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.DataInteraction
import com.agoda.kakao.common.actions.BaseActions
import com.agoda.kakao.common.builders.ViewBuilder
import com.agoda.kakao.common.views.KBaseView
import com.mintrocket.testcore.checkMatches
import org.hamcrest.Matcher

class KViewGroup : KBaseView<KViewGroup>, BaseActions, KGroupAssertions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(
        parent: Matcher<View>,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)

    constructor(
        parent: DataInteraction,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)
}

class KNestedScrollView : KBaseView<KNestedScrollView>, KNestedScrollActions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(
        parent: Matcher<View>,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)

    constructor(
        parent: DataInteraction,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)
}

/*class KViewWithCustoms : KBaseView<KViewWithCustoms>, KCustomActions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(
        parent: Matcher<View>,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)

    constructor(
        parent: DataInteraction,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)
}*/

interface KNestedScrollActions : BaseActions {
    fun scrollToBottom() {
        view.perform(CustomActions.scrollNestedToBottom())
    }
}

interface KGroupAssertions : BaseActions {
    fun withChildCountK(count: Int) {
        view.checkMatches(CustomMatchers.withChildCount(count))
    }
}

class KToolbar : KBaseView<KToolbar>, KToolbarAssertions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(
        parent: Matcher<View>,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)

    constructor(
        parent: DataInteraction,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)
}

interface KToolbarAssertions : BaseActions {
    fun withTitle(title: String) {
        view.checkMatches(CustomMatchers.withTitle(title))
    }

    fun withTitleRes(@StringRes title: Int) {
        view.checkMatches(CustomMatchers.withTitleRes(title))
    }
}

class KCompoundButton : KBaseView<KCompoundButton>, KCompoundButtonActions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(
        parent: Matcher<View>,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)

    constructor(
        parent: DataInteraction,
        function: ViewBuilder.() -> Unit
    ) : super(parent, function)
}

interface KCompoundButtonActions : BaseActions {
    fun setChecked(checked: Boolean) {
        view.perform(CustomActions.setChecked(checked))
    }
}