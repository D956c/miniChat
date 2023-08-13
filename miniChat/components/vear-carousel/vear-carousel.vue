<template>
	<swiper class="image-container" previous-margin="45rpx" next-margin="45rpx" circular
		style="background-color: darkgreen;" @change="swiperChange">
		<swiper-item :class="currentIndex == index ? 'swiper-item' : 'swiper-item-side'"
			v-for="(item, index) in imgList" :key="item[urlKey]">
			<image @click="clickImg(item)" :class="currentIndex == index ? 'item-img' : 'item-img-side'"
				:src="item[urlKey]" lazy-load :style="dontFirstAnimation ? 'animation: none;' : ''" mode="aspectFill">
			</image>
		</swiper-item>
	</swiper>
</template>
<script>
	export default {
		props: {
			imgList: {
				type: Array,
				default () {
					return []
				}
			},
			urlKey: {
				type: String,
				default () {
					return ''
				}
			},
		},
		data() {
			return {
				currentIndex: 0,
				dontFirstAnimation: true
			}
		},
		methods: {
			swiperChange(e) {
				this.dontFirstAnimation = false
				this.currentIndex = e.detail.current
			},
			clickImg(item) {
				// this.$emit('selected', item, this.currentIndex)

				wx.previewImage({
					urls: [item.url], //需要预览的图片http链接列表，多张的时候，url直接写在后面就行了
					current: '', // 当前显示图片的http链接，默认是第一个
					success: function(res) {},
					fail: function(res) {},
					complete: function(res) {},
				})

			}
		}
	}
</script>
<style scoped>
	.image-container {
		width: 750rpx;
		height: 350rpx;
	}

	.item-img {
		width: 630rpx;
		height: 300rpx;
		border-radius: 14rpx;
		animation: to-big .3s;
	}

	.swiper-item {
		width: 630rpx;
		height: 300rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.item-img-side {
		width: 630rpx;
		height: 260rpx;
		border-radius: 14rpx;
		animation: to-mini .3s;
	}

	.swiper-item-side {
		width: 630rpx;
		height: 260rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	@keyframes to-mini {
		from {
			height: 300rpx;
		}

		to {
			height: 260rpx;
		}
	}

	@keyframes to-big {
		from {
			height: 260rpx;
		}

		to {
			height: 300rpx;
		}
	}
</style>
