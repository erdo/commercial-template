package foo.bar.clean.domain

// just a hack to slow everything down so that we can check the edge cases
// where we need to show the loading spinner etc
const val SLOMO = true
// We're using mocky.io and apollo as a free server side, so if they are
// down, switch to OFFLINE mode and rebuild
const val OFFLINE_MOCKY = true
const val OFFLINE_APOLLO = false
