export default {
  // #ifndef H5
  baseUrl: "http://127.0.0.1:9999",
  // #endif
  // #ifdef H5
  baseUrl: "/api",
  // #endif
  socketUrl: "ws://127.0.0.1:9999/socket/",
  env: "dev",
};