package com.sumeet.insta_fireclone.daos

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sumeet.insta_fireclone.models.Post
import com.sumeet.insta_fireclone.models.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


private const val TAG = "PostDao"

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postsCollection = db.collection("posts")
    val auth = Firebase.auth

    fun addPost(description:String,imageUrlUpload:String) {
        val currentUser = auth.currentUser
        GlobalScope.launch {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUser!!.uid).await().toObject(User::class.java)!!
            val currentTime = System.currentTimeMillis()
            val post = Post(description,user,currentTime,imageUrlUpload)
            postsCollection.document().set(post)
        }
    }


    fun getPostById(postId:String): Task<DocumentSnapshot> {
        return postsCollection.document(postId).get()
    }


    fun updateLikes(postId:String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)
            val isLiked = post!!.likedBy.contains(currentUserId)

            if(isLiked) {
                post.likedBy.remove(currentUserId)
            }
            else {
                post.likedBy.add(currentUserId)
            }
            postsCollection.document(postId).set(post)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deletePost(postId:String) {
        Log.i(TAG,"Problem Occured-3")
        GlobalScope.launch(Dispatchers.Unconfined) {
            Log.i(TAG,"Problem Occured-4")
            val post = getPostById(postId).await().toObject(Post::class.java)
            val fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(post!!.imageUrlPost)
            Log.i(TAG,"Problem Occured-5")
            fileRef.delete()
            Log.i(TAG,"Problem Occured-6")

            postsCollection.document(postId).delete().await()
            Log.i(TAG,"Problem Occured-7")
        }
    }

}