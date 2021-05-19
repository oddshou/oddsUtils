package in.srain.cube.views.wt.util;

import in.srain.cube.views.wt.PtrFrameLayout;

/**
 * Created by Carl on 2017/2/4.
 * 作用：
 */

public class PtrUtil {
    
    
    
    public static void initPtrProperty(PtrFrameLayout layout) {
        if (layout == null) {
            return;
        }
        layout.setResistance(1.3f); //设置下拉的阻尼系数，值越大感觉越难下拉
//        layout.setRatioOfHeaderHeightToRefresh(1.0f); //设置超过头部的多少时，释放可以执行刷新操作
        layout.setDurationToClose(200); //设置下拉回弹的时间
        layout.setDurationToCloseHeader(100);   //设置刷新完成，头部回弹时间，注意和前一个进行区别
        layout.setPullToRefresh(false); // 设置下拉过程中执行刷新，我们一般设置为false
        layout.setKeepHeaderWhenRefresh(true);  //设置刷新的时候是否保持头部
        layout.disableWhenHorizontalMove(true);
        layout.disableWhenWorkingAfterRelease(true);
    }
}
