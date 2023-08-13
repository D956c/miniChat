<template>
	<div @longpress="long">
		<view v-if="showTime" class="flex align-center justify-center pb-4 pt-2">
			<text class="font-sm text-light-muted">{{showTime}}</text>
			<!-- 	<text>{{item.create_time}}</text> -->
		</view>
		<!-- 气泡 -->
		<view class="flex align-start  position-relative mb-3" :class="isself?'justify-end':'justify-start'">
			<!-- 好友 -->
			<template v-if="!isself">
				<free-avater size="70" :src="item.senderAvatar" class="rounded-circle"></free-avater>
				<text v-if="hasLabelClass"
					class="chat-left-icon iconfont text-white  font-md position-absolute">&#xe68a;</text>

			</template>

			<div class="p-2 rounded" style="max-width: 500rpx;" :class="labelClass">
				<!-- 文字 -->
				<text v-if="item.messageType==='text'" class="font-md" style="color: black;">{{item.content}}</text>
				<!-- 表情 -->
				<image v-else-if="item.type==='emoji'" :src="item.data" lazy-load mode="widthFix"
					style="width: 50rpx;height: 50rpx;"></image>
				<!-- 图片 -->
				<free-image v-else-if="item.type==='image'" :src="item.data" imageClass="rounded" :maxWidth="400"
					:maxHeight="350" @click="preview(item.data)"></free-image>

				<!-- <image v-else-if="item.type==='image'" :src="item.data" lazy-load mode="widthFix"
					style="width: 350rpx;height: 350rpx;" class="rounded" 
					@click="preview(item.data)" @load="loadImage"></image> -->
				<!-- 视频 -->

				<view v-else-if="item.type === 'video'" class="position-relative rounded" @tap="openVideo"
					@longpress="stop">
					<free-image :src="item.options.poster" imageClass="rounded" :maxWidth="400" :maxHeight="350"
						@load="loadPoster"></free-image>
					<text class="iconfont text-white position-absolute"
						style="font-size: 80rpx;width: 80rpx;height: 80rpx;" :style="posterIconStyle">&#xeca9;</text>
				</view>


			</div>
			<!-- 自己 -->
			<template v-if="isself">
				<text v-if="hasLabelClass" class="chat-right-icon iconfont  font-md position-absolute">&#xeca9;</text>
				<free-avater size="70" :src="item.senderAvatar" class="rounded-circle"></free-avater>

			</template>

		</view>
		<view v-if="item.sendStatus==='fail'" class="flex align-center justify-end px-4">
			<text class="font-sm text-hover-danger">发送失败!</text>
		</view>

	</div>

</template>

<script>
	import freeAvater from '@/components/free-ui/free-avater.vue'
	import $T from '@/common/time.js';
	import freeImage from '@/components/free-ui/free-image.vue';
	import {
		mapState
	} from 'vuex';
	export default {
		components: {
			freeAvater,
			freeImage
		},
		props: {
			item: Object,
			index: Number,
			pretime: [Number, String]
		},
		data() {
			return {
				// 视频默认封面宽高
				poster: {
					w: 100,
					h: 100
				}
			}
		},
		computed: {
			...mapState({
				user: state => state.user.user
			}),
			// 判断是否本人
			isself() {
				let id = this.user.id ? this.user.id : 0
				return this.item.senderId === id
			},
			// 显示时间
			showTime() {

				return $T.getChatTime(this.item.createTime, this.pretime)
			},
			// 是否需要气泡样式
			hasLabelClass() {
				return this.item.messageType === 'text' || this.item.messageType === 'emoji'
			},
			// 气泡样式
			labelClass() {
				let label = this.hasLabelClass ? 'bg-chat-item mr-3' : 'mr-3'
				return this.isself ? label : 'bg-white ml-3'
			},
			// 短视频封面图标位置
			// posterIconStyle(){
			// 	let w=this.poster.w/2-uni.upx2px(80)/2
			// 	let h=this.poster.h/2-uni.upx2px(80)/2
			// 	return `left:${w}px;top:${h}px;`
			// }
			// 短视频封面图标位置
			posterIconStyle() {
				let w = this.poster.w / 2 - uni.upx2px(80) / 2
				let h = this.poster.h / 2 - uni.upx2px(80) / 2
				return `left:${w}px;top:${h}px;`
			}
		},
		methods: {
			// 预览视频
			openVideo() {
				uni.navigateTo({
					url: '/pages/chat/video/video?url=' + this.item.data,
					// success: res => {},
					// fail: () => {},
					// complete: () => {}
				});
			},
			// 加载封面
			loadPoster(e) {
				this.poster.w = e.w
				this.poster.h = e.h
			},
			// 长按事件
			long(e) {
				let x = 0
				let y = 0
				// #ifdef APP-PLUS-NVUE
				if (Array.isArray(e.changedTouches) && e.changedTouches.length > 0) {
					x = e.changedTouches[0].screenX
					y = e.changedTouches[0].screenY
				}
				// #endif
				// #ifdef H5
				x = e.changedTouches[0].pageX
				y = e.changedTouches[0].pageY
				// #endif
				// #ifdef MP
				x = e.detail.x
				y = e.detail.y
				// #endif
				this.$emit('long', {
					x,
					y,
					index: this.index
				})
			},
			// 预览图片
			preview(url) {
				this.$emit('preview', url)
			},

		},

	}
</script>

<style>
	.chat-left-icon {
		left: 80rpx;
		top: 20rpx;
	}

	.chat-right-icon {
		right: 80rpx;
		top: 20rpx;
		color: #00CFFD;
	}
</style>