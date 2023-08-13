<template>
  <view class="custom-refresh">
    <image
      :src="refreshing ? '../../static/images/defult/1.gif' : '../../static/logo.png'"
      mode="aspectFit"
      v-show="showRefresh"
    ></image>
    <view class="refresh-text">{{ refreshText }}</view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      showRefresh: false,
      refreshing: false,
      refreshText: '下拉刷新'
    };
  },
  methods: {
    onTouchStart(event) {
      // 记录初始位置
      this.startY = event.touches[0].clientY;
    },
    onTouchMove(event) {
      // 计算移动距离
      const deltaY = event.touches[0].clientY - this.startY;
      // 如果下拉距离超过50，则显示下拉刷新组件
      if (deltaY > 50) {
        this.showRefresh = true;
        // 设置下拉刷新的文本提示
        this.refreshText = '释放刷新';
      } else {
        this.refreshText = '下拉刷新';
      }
    },
    onTouchEnd() {
      if (this.showRefresh) {
        this.refreshing = true;
        // 模拟数据加载
        setTimeout(() => {
          // 数据加载完成后，结束刷新
          this.refreshing = false;
          this.showRefresh = false;
          this.refreshText = '下拉刷新';
          uni.showToast({
            title: '刷新成功',
            icon: 'success'
          });
        }, 2000);
      }
    }
  }
};
</script>

<style scoped>
.custom-refresh {
  text-align: center;
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ccc;
}

.refresh-text {
  height: 30px;
  line-height: 30px;
}
</style>