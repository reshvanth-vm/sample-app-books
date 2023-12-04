package com.example.books.core.data

import com.example.books.core.model.User
import com.example.books.core.usecase.*
import com.example.books.core.usecase.collection.*
import com.example.books.core.usecase.profile.*
import com.example.books.core.usecase.store.UserStoreBooksPagingDataFlow
import kotlinx.coroutines.flow.Flow

interface UserRepo : UserAuthenticator,
                     SignUpUser,
                     AddBooksToUserCollection,
                     BookmarkBooks,
                     UserStoreBooksPagingDataFlow,
                     RemoveBooksFromUserCollection,
                     RemoveBooksFromUserBookmarks,
                     IsUserCollectedBookFlow,
                     NewUserEmailValidator,
                     SignOutUser,
                     NewUserPwdValidator,
                     GetRecentlyLoggedInUserEmails,
                     SignInUser,
                     UserAvatarFlow,
                     CurrentUserFlow,
                     IsUserBookmarkedBookFlow {

  val userFlow: Flow<User?>
}
