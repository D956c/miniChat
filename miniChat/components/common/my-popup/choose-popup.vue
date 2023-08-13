<template>
	<view v-if="visible">
		<view class="right-dropdown" @click.stop="">
			<view class="mask" @click="closeDropdown"></view>
			<!-- <text class="iconfont icon-dianzan1" 
					style="font-size: 50rpx; margin-right: 20rpx;"></text> -->
			<view class="dropdown-content" :style="'top:'+statusBarHeight1+'px'">
				<!-- 例如下拉框中的选项列表 -->
				<view class="item flex" @click="handleClick('toCreateGroup')">
					<text class="flex-1 font-weight-light font">创建星群</text>
					<image class="" src="/static/images/choose-popup/star1.png" style="height: 50rpx;width: 50rpx;">
					</image>
				</view>
				<view class="item flex" @click="handleClick('toUserList')">
					<text class="flex-1 font-weight-light font">通讯录</text>
					<image src="/static/images/choose-popup/star2.png" style="height: 50rpx;width: 50rpx;"></image>
				</view>

				<!-- 可以根据需要进行动态渲染内容 -->
			</view>

		</view>



	</view>

</template>
<script>
	export default {
		components: {

		},
		props: {
			visible: {
				type: Boolean,
				default: false
			}
		},

		data() {
			return {
				//visible: false // 将prop的值作为初始化值
				myData: this.visible,
				// 状态栏
				statusBarHeight1: 0

			};
		},
		onLoad() {
			// #ifdef APP-PLUS-NVUE
			this.statusBarHeight1 = plus.navigator.getStatusbarHeight()
			// #endif
			// uni.getSystemInfo({
			// 	success: res => {
			// 		console.log("jjj",res);
			// 		this.statusBarHeight = res.statusBarHeight;
			// 		console.log("hhh",this.statusBarHeight);
			// 		//this.scrollH = res.windowHeight - uni.upx2px(100) - this.statusBarHeight;
			// 	}
			// })

		},
		mounted() {
			// #ifdef APP-PLUS-NVUE
			var statusBarHeight = plus.navigator.getStatusbarHeight()
			console.log("导航", statusBarHeight)
			this.statusBarHeight1 = statusBarHeight + uni.upx2px(90)
			// #endif

			
			console.log("statusBarHeight1", this.statusBarHeight1)
		},
		methods: {
			closeDropdown() {
				// 关闭弹出框
				console.log("遮罩")
				uni.$emit('msg-popup', {
					msg: false
				});

			},
			handleClick(data) {
				// 假设传递一个值为data的数据到父组件
				this.$emit('customEvent', data);
			}
		}
	};
</script>
<style>
	.mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0);

		/* 设置遮罩层的层级，确保遮罩层位于弹出框上方 */
	}

	.right-dropdown {
		position: fixed;
		overflow: hidden;
		top: 0;
		right: 0;
		bottom: 0;
		z-index: 999;
		/* 设置合适的z-index用于覆盖其他元素 */
		/* 其他样式，例如背景色、宽度等 */
	}

	.dropdown-content {
		position: fixed;
		top: 10rpx;
		right: 30rpx;
		width: 250rpx;
		padding: 10rpx;
		/* 下拉框宽度，根据需要进行调整 */
		background-color: #ffffff;
		border-radius: 20rpx;
		/* 下拉框背景色 */
		/* 其他样式，例如边框、阴影等 */
		box-shadow: 0 2px 4px 0 rgba(0, 123, 255, 0.2);
	}

	.item {
		padding: 10rpx;
		justify-content: center;
		/* border-bottom: 1px solid #eee; */
		/* 选项之间的分隔线 */
		/* 其他选项样式，例如字体颜色、字号等 */
	}
</style>