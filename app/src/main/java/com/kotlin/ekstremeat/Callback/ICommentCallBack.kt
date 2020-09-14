package com.kotlin.ekstremeat.Callback

import com.kotlin.ekstremeat.Model.CommentModel

interface ICommentCallBack {
    fun onCommentLoadSuccess(commentList:List<CommentModel>)
    fun onCommentLoadFailed(message:String)
}