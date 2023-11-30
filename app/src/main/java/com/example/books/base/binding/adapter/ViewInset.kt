package com.example.books.base.binding.adapter

import androidx.core.graphics.Insets

class ViewInset(var left: Int, var top: Int, var right: Int, var bottom: Int) {

  companion object {
    const val TOP = 1
    const val LEFT = 2
    const val BOTTOM = 4
    const val RIGHT = 8
  }

  fun obtainInsets(insets: Insets, required: Int) {
    if (required.and(LEFT) == LEFT) {
      left = insets.left
    }
    if (required.and(TOP) == TOP) {
      top = insets.top
    }
    if (required.and(RIGHT) == RIGHT) {
      right = insets.right
    }
    if (required.and(BOTTOM) == BOTTOM) {
      bottom = insets.bottom
    }
//    if (required.and(LEFT) == LEFT) {
//      left += insets.left
//    }
//    if (required.and(TOP) == TOP) {
//      top += insets.top
//    }
//    if (required.and(RIGHT) == RIGHT) {
//      right += insets.right
//    }
//    if (required.and(BOTTOM) == BOTTOM) {
//      bottom += insets.bottom
//    }
  }
}