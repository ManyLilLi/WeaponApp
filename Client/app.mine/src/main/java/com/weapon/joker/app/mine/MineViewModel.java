package com.weapon.joker.app.mine;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.weapon.joker.app.mine.login.LoginActivity;
import com.weapon.joker.app.stub.share.IShareListener;
import com.weapon.joker.app.stub.share.ShareParams;
import com.weapon.joker.app.stub.share.ShareType;
import com.weapon.joker.lib.middleware.utils.LogUtils;
import com.weapon.joker.lib.middleware.utils.share.ShareView;
import com.weapon.joker.lib.net.bean.UserBean;
import com.weapon.joker.lib.net.data.UserData;

/**
 * class：   WeaponApp
 * author：  xiaweizi
 * date：    2017/9/17 11:01
 * e-mail:   1012126908@qq.com
 * desc:
 */
public class MineViewModel extends MineContact.ViewModel implements IShareListener, IUiListener {

    private ShareView mShareView;

    private String userName;
    private boolean hasLogin = false;
    private UserBean mUserBean;

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    @Bindable
    public boolean getHasLogin() {
        return hasLogin;
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo() {
        mUserBean = UserData.getInstance().getUserBean(getContext());
        if (TextUtils.isEmpty(mUserBean.token)) {
            // 未登录
            setUserName("我的");
            setHasLogin(false);
        } else {
            // 已经登录
            setUserName(mUserBean.user);
            setHasLogin(true);
        }
    }

    /**
     * 初始化分享
     */
    public void initShare() {
        ShareParams params = new ShareParams.Builder().setTitle(getContext().getResources().getString(R.string.share_name))
                .setDescription(getContext().getResources().getString(R.string.share_desc))
                .setAppUrl(getContext().getResources().getString(R.string.share_url))
                .setImgUrl(getContext().getResources().getString(R.string.share_img_url))
                .setResId(R.mipmap.ic_launcher_round)
                .build();
        mShareView = new ShareView(getContext(), params, this);
    }

    /**
     * 点击分享按钮
     * @param view
     */
    public void shareOnClick(View view) {
        MobclickAgent.onEvent(getContext(), "share_click");
        mShareView.show();
    }

    public void loginOnClick(View view) {
        if (getHasLogin()) {
            Toast.makeText(getContext().getApplicationContext(), "已经登录", Toast.LENGTH_SHORT).show();
            // TODO: 2017/10/13 跳转到个人中心界面
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(((Activity) getContext())).toBundle());
//            PublicActivity.startActivity((Activity) getContext(), "com.weapon.joker.app.mine.login.LoginRegisterFragment");
        }
    }

    public IUiListener getIUiListener() {
        return this;
    }


    /*************************** 分享事件相关的回调 ***************************/
    @Override public void onShareSuccess(ShareType shareType, String s) {
        Toast.makeText(getContext(), shareType.toString() + ":\t分享成功", Toast.LENGTH_SHORT).show();
        LogUtils.logi("Share result:\t" + "type:\t" + shareType.toString() + "desc:\t" + s);
    }

    @Override public void onShareFailed(ShareType shareType, String s) {
        Toast.makeText(getContext(), shareType.toString() + ":\t分享失败", Toast.LENGTH_SHORT).show();
        LogUtils.logi("Share result:\t" + "type:\t" + shareType.toString() + "desc:\t" + s);

    }

    @Override public void onShareCancel(ShareType shareType) {
        Toast.makeText(getContext(), shareType.toString() + ":\t分享取消", Toast.LENGTH_SHORT).show();
        LogUtils.logi("Share result:\t" + "type:\t" + shareType.toString());

    }

    @Override public void onComplete(Object o) {LogUtils.logi("QQ Share result:\tonComplete!" + o.toString());}
    @Override public void onError(UiError uiError) {LogUtils.logi("QQ Share result:\tonError!" + uiError.errorMessage);}
    @Override public void onCancel() {LogUtils.logi("QQ Share result:\tonCancel!");}
    /************************************************************************/



}
