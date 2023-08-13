<template>
	<view>
<!-- 		<uni-swipe-action>@click="openChat(item.id,item.sessionId)"
			<uni-swipe-action-item v-for="(item1, index) in swipeList" :right-options="item1.options" :key="item1.id"
				> -->
				<view class="flex align-center p-3" hover-class="bg-light" 
				@click="$emit('click')">
					<image :src="item.avatar" style="height: 100rpx;width: 100rpx;" class="rounded-circle mr-2">
					</image>
					<view class="flex flex-column flex-1">
						<view class="flex align-center justify-between">
							<text class="font-md">{{item.name}}</text>
							<text class="uni-text text-secondary">{{item.update_time | formatTIme}}</text>
						</view>
						<view class="flex align-center justify-between">
							<text class="text-secondary text-ellipsis font-weight-light font"
								style="max-width: 400rpx;">{{item.data}}</text>
							<uni-badge :text="item.noreadnum"></uni-badge>
						</view>
					</view>

				</view>
		
<!-- 				
			</uni-swipe-action-item>
		</uni-swipe-action>
 -->
	</view>
</template>

<script>
	import $T from '@/common/time.js';
	import uniBadge from "@/components/uni-ui/uni-badge/components/uni-badge/uni-badge.vue"
	import uniSwipeAction from '../uni-ui/uni-swipe-action/components/uni-swipe-action/uni-swipe-action.vue'
	import uniSwipeActionItem from '../uni-ui/uni-swipe-action/components/uni-swipe-action-item/uni-swipe-action-item.vue'
	export default {
		components: {
			uniBadge,
			// uniSwipeAction,
			// uniSwipeActionItem
		},
		data() {
			return {
				swipeList: [{
					id: 1,
					options: [{
							text: '置顶'
						},
						{
							text: '标记为已读',
							style: {
								backgroundColor: 'rgb(254,156,1)'
							}
						},
						{
							text: '删除',
							style: {
								backgroundColor: 'rgb(255,58,49)'
							}
						}
					],
					// content: 'item3'
				}]
			};

		},

		props: {
			item: Object,
			index: Number
		},

		// 过滤器
		filters: {
			formatTIme(value) {
				return $T.gettime(value);
			}
		},
		methods:{
			openChat(){
				uni.navigateTo({
					url: '/pages/chat/chat?friend_id=' + friend_id + '&session_id=' + session_id + '&type=user',
					
				});
			}
		}

	}
</script>

<style>
</style>
