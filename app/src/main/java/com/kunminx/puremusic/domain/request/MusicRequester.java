/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.puremusic.domain.request;


import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.data.repository.DataRepository;

/**
 * 音乐资源  Request
 *
 * TODO tip 1：基于 "单一职责原则"，应将 Jetpack ViewModel 框架划分为 state-ViewModel 和 result-ViewModel，
 * result-ViewModel 作为领域层组件，提取和继承 Jetpack ViewModel 框架中 "作用域管理" 的能力，
 * 使业务实例能根据需要，被单个页面独享，或多个页面共享，
 *
 * result-ViewModel 作为领域层组件，职责仅限于 "业务逻辑处理 和 消息分发"，
 * UI 逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * 数据总是来自领域层业务逻辑的处理，并单向回推至 UI 层，在 UI 层中响应数据的变化，
 *
 * 故此应根据上述介绍明确区分 UI 逻辑和业务逻辑，不要将 UI 逻辑理解为业务逻辑的一部分，
 * result-ViewModel 中应当只关注数据的生成，而不关注数据的使用，
 *
 * 如这么说无体会，详见《再回首 页面搭档 Jetpack ViewModel》解析。
 * https://xiaozhuanlan.com/topic/6018295743
 *
 * TODO tip 2：应顺应 "响应式编程"，做好 "单向数据流" 开发，
 *
 * 常见消息分发场景包括：数据请求，页面间通信等，
 * 数据请求由 Requester 负责，页面通信由 PageMessenger 负责，
 *
 * 所有事件都可交由 "鉴权中心" 在内部决策和处理，并统一分发结果给所有订阅者页面。
 *
 * 如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
 * https://xiaozhuanlan.com/topic/6017825943
 *
 *
 * TODO tip 3：Requester 通常按业务划分
 * 一个项目中通常存在多个 Requester 类，
 * 每个页面可根据业务需要持有多个不同 Requester 实例。
 *
 * requester 职责仅限于 "业务逻辑处理" 和 "消息分发"，不建议在此处理 UI 逻辑，
 * UI 逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * 数据总是来自领域层业务逻辑的处理，并单向回推至 UI 层，在 UI 层中响应数据的变化（也即处理 UI 逻辑），
 * 换言之，UI 逻辑只适合在 Activity/Fragment 等视图控制器中编写，将来升级到 Jetpack Compose 更是如此。
 *
 *
 * 如这么说无体会，详见《Jetpack MVVM 分层设计解析》解析
 * https://xiaozhuanlan.com/topic/6741932805
 *
 *
 * Create by KunMinX at 19/10/29
 */
public class MusicRequester extends ViewModel {

    private final MutableResult<DataResult<TestAlbum>> mFreeMusicsResult = new MutableResult<>();

    //TODO tip 3：MutableResult 应仅限 "可信源" 内部使用，且只暴露 immutable Result 给 UI 层，
    //如此达成 "可信源" 设计，也即通过 "访问控制权限" 实现 "读写分离"，

    //如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
    //https://xiaozhuanlan.com/topic/6017825943

    public Result<DataResult<TestAlbum>> getFreeMusicsResult() {
        return mFreeMusicsResult;
    }

    public void requestFreeMusics() {

        //TODO tip 4：为方便语义理解，此处直接将 DataResult 作为 LiveData value 回推给 UI 层，
        //而非 DataResult 泛型实体拆下来单独回推，如此
        //一方面使 UI 层有机会基于 DataResult 的 responseStatus 分别处理 "请求成功或失败" 情况下 UI 表现，
        //另一方面从语义上强调了 该结果是请求得来的只读数据，与 "可变状态" 形成明确区分，
        //从而方便团队开发人员自然而然遵循 "可信源"/"单向数据流" 开发理念，规避消息同步一致性等不可预期错误。

        //如这么说无体会，详见《这是一份 “架构模式” 自驾攻略》中对 "只读数据" 和 "可变状态" 区别的解析。
        //https://xiaozhuanlan.com/topic/8204519736

        //TODO Tip 5：lambda 语句只有一行时可简写，具体可结合实际情况选择和使用

        /*DataRepository.getInstance().getFreeMusic(dataResult -> {
            mFreeMusicsLiveData.setValue(dataResult);
        });*/

        DataRepository.getInstance().getFreeMusic(mFreeMusicsResult::setValue);
    }
}
