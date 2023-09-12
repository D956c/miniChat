<template>

	<view>
		<view class="uni-uploader rounded " v-if="showvideo"
			style="padding: 10rpx;background-color: #ffffff; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
			<video :src="src" class="video" object-fit="contain" direction="0" style="border-radius: 10rpx;"></video>
		</view>
	</view>
</template>
<script>
	var sourceType = [
		['camera'],
		['album'],
		['camera', 'album']
	]
	export default {
		props: {
			showvideo: {
				type: Boolean,
				default: true
			},
			show2: {
				type: Boolean,
				default: true
			}
		},
		data() {
			return {
				title: 'chooseVideo',
				vlogList: [],
				sourceTypeIndex: 2,
				sourceType: ['拍摄', '相册', '拍摄或相册'],
				src: '',
				demo: '',
				cameraList: [{
						value: 'back',
						name: '后置摄像头',
						checked: 'true'
					},
					{
						value: 'front',
						name: '前置摄像头'
					},
				],
				cameraIndex: 0,

			}
		},

		onUnload() {
			this.src = '',
				this.sourceTypeIndex = 2,
				this.sourceType = ['拍摄', '相册', '拍摄或相册'];
		},
		mounted() {
			this.vlogList = this.list
		},
		methods: {
			radioChange(evt) {
				for (let i = 0; i < this.cameraList.length; i++) {
					if (this.cameraList[i].value === evt.detail.value) {
						this.cameraIndex = i;
						break;
					}
				}
			},
			sourceTypeChange: function(e) {
				this.sourceTypeIndex = parseInt(e.detail.value)
			},
			chooseVideo: function() {
				uni.chooseVideo({
					camera: this.cameraList[this.cameraIndex].value,
					// sourceType: sourceType[this.sourceTypeIndex],
					sourceType: ['album'],
					compressed: false,
					success: (res) => {
						this.src = res.tempFilePath
						// 重要将组件中的src传入引用该组件的页面
						this.$emit('change', this.src)
						// this.getVideoPoster(this.src)
					},
					fail: (err) => {
						// #ifdef MP
						uni.getSetting({
							success: (res) => {
								let authStatus = false;
								switch (this.sourceTypeIndex) {
									case 0:
										authStatus = res.authSetting['scope.camera'];
										break;
									case 1:
										authStatus = res.authSetting['scope.album'];
										break;
									case 2:
										authStatus = res.authSetting['scope.album'] && res
											.authSetting['scope.camera'];
										break;
									default:
										break;
								}
								if (!authStatus) {
									uni.showModal({
										title: '授权失败',
										content: 'Hello uni-app需要从您的相机或相册获取视频，请在设置界面打开相关权限',
										success: (res) => {
											if (res.confirm) {
												uni.openSetting()
											}
										}
									})
								}
							}
						})
						// #endif
					}
				})
			}
		}
	}
</script>

<style>
	.video {
		width: 100%;
		/* 	height: auto; */
	}

	.camera-tips {
		padding: 10rpx 30rpx;
	}
</style>
