package com.example.revup._DATACLASS

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class Brand(
    var id: Int = 0,
    var name: String = "",

    var models: MutableSet<Model> = HashSet()
)

class Car(
    var id: Int?,
    var memberId: Int,
    var modelId: Int,
    var modelYear: Int? = null,
    var horsePower: Double,
    var description: String? = null,
    var picture: String? = null,

    var member: Member? = null,
    var model: Model? = null
)

class Club(
    var id: Int?,
    var name: String,
    var founder: Int,
    var description: String?,
    var picture: String?,
    var creationDate: String,

//    var member: Member? = null,
//    var clubEvent: MutableList<ClubEvent> = mutableListOf(),
//    var memberClub: MutableList<MemberClub> = mutableListOf()
)

class ClubEvent(
    var id: Int?,
    var name: String,
    var address: String,
    var clubId: Int,
    var picture: String?,
    var startDate: String,
    var routeStartDate: String? = null,
    var endDate: String,
    var description: String,
    var state: Int,
    var lat: Double,
    var long: Double,

    var club: Club? = null,
    var eventState: EventState? = null,
    var bitmap: Bitmap? = null
)

class Post (
    var id: Int? = 0,
    var title: String? = null,
    var postType: Int = 0,
    var description: String? = null,
    var postDate: String,
    var picture: String? = null,
    var likes: Long = 0,
    var routeId: Int? = null,
    var memberId: Int = 0,
    var comments: Long = 0,
    var location_id: Int? = null,

    var liked: Boolean = false,

    var postType1: PostType? = null,
    var route: Route? = null,
    var postComments: MutableSet<PostComment> = HashSet(),
    var member: Member? = null,
    var members: MutableSet<Member> = HashSet(),
    var location: MemberLocation? = null
)

class Model (
    var id: Int = 0,
    var idBrand: Int = 0,
    var modelName: String? = null,

    var brand: Brand? = null,
    var cars: MutableSet<Car> = HashSet()
)

class PostComment (
    var id: Int = 0,
    var postId: Int = 0,
    var memberId: Int = 0,
    var commentContent: String = "",
    var datetime: String,

    var post: Post? = null,
    var member: Member? = null
)

class Message(
    var senderId: Int,
    var receiverId: Int,
    var datetime: java.util.Date,
    var contentMessage: String,
    var stateId: Int,

    var messageState: MessageState?,
    var member: Member?,
    var member1: Member?
)

class MessageState (
    var id: Int = 0,
    var name: String? = null,

    var messages: MutableSet<Message> = HashSet()
)

class MemberRelation (
    var memberId1: Int = 0,
    var memberId2: Int = 0,
    var stateId: Int = 0,

    var relationState: RelationState? = null,
    var member: Member? = null,
    var member1: Member? = null
)

class MemberLocation (
    var id: Int?,
    var municipality: String? = null,
    var ccaa: String? = null,
    var country: String? = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,

    var members: MutableSet<Member> = HashSet()
)

class MemberClubRole (
    var id: Int = 0,
    var name: String = "",

    //var memberClub: MutableSet<MemberClub> = HashSet()
)

class Member (
    var id: Int? = null,
    var name: String? = null,
    var membername: String? = null,
    var experience: Int? = 0,
    var email: String? = null,
    var genderId: Int = 0,
    var locationId: Int = 0,
    var dateOfBirth: String,
    var loginDate: String,
    var description: String? = null,
    var profilePicture: String? = null,
    var password: String? = null,

//    var cars: MutableSet<Car> = HashSet(),
//    var clubs: MutableSet<Club> = HashSet(),
//    var gender: Gender? = null,
//    var memberLocation: MemberLocation? = null,
//    var memberClub: MutableSet<MemberClub> = HashSet(),
//    var memberRelation: MutableSet<MemberRelation> = HashSet(),
//    var memberRelation1: MutableSet<MemberRelation> = HashSet(),
//    var messages: MutableSet<Message> = HashSet(),
//    var messages1: MutableSet<Message> = HashSet(),
//    var posts: MutableSet<Post> = HashSet(),
//    var postComment: MutableSet<PostComment> = HashSet(),
//    var routes: MutableSet<Route> = HashSet(),
//    var posts1: MutableSet<Post> = HashSet()
)

class Gender (
    var id: Int = 0,
    var name: String = "",

    var members: MutableSet<Member> = HashSet()
)

class EventState (
    var id: Int = 0,
    var name: String? = null,

    var clubEvent: MutableSet<ClubEvent> = HashSet()
)

class MemberClub (
    var memberId: Int = 0,
    var clubId: Int = 0,
    var roleType: Int = 0,
    var joinDate: String,

    var memberClubRole: MemberClubRole? = null,
    var club: Club? = null,
    var member: Member? = null
)

class PostType (
    var id: Int = 0,
    var name: String? = null,

    var posts: MutableSet<Post> = HashSet()
)

class RelationState (
    var id: Int = 0,
    var name: String = "",

    var memberRelation: MutableSet<MemberRelation> = HashSet()
)

class Route (
    var id: Int? = 0,
    var name: String? = null,
    var waypoints: String? = null,
    var distance: BigDecimal? = BigDecimal.ZERO,
    var duration: Long = 0,
    var maxElevation: BigDecimal? = null,
    var elevationGain: BigDecimal? = null,
    var startAddress: String? = null,
    var endAddress: String? = null,
    var terrainTypeId: Int? = 1,
    var description: String? = null,
    var memberId: Int = 0,
    var datetime: String? = null,

    var terrainType: TerrainType? = null,
    var posts: MutableSet<Post> = HashSet(),
    var member: Member? = null
)

class TerrainType (
    var id: Int = 0,
    var name: String = "",

    var routes: MutableSet<Route> = HashSet()
)

class Notification(
    var title: String,
    var description: String,
    var content: String,
    var imageContent: String,
    var icon: Int,
    var priority: Int,
    var date: String,
    var seen: Boolean,
    var activity: String
)

fun FormatDate(date: String): Date {
    var formater: DateTimeFormatter? = null
    var formats: List<String> = listOf("dd/MM/yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SS", "yyyy-MM-dd'T'HH:mm:ssss", "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.S")
    var local_datetime: LocalDateTime? = null
    var local_date: LocalDate? = null

    for (format in formats){
        try{
            formater = DateTimeFormatter.ofPattern(format)
            local_datetime = LocalDateTime.parse(date,formater)
            break
        }catch(e: Exception){
            try{
                formater = DateTimeFormatter.ofPattern(format)
                local_date = LocalDate.parse(date,formater)
                break
            }catch (e: Exception){
                continue
            }
        }
    }

    if(local_datetime == null){
        return Date.from(local_date!!.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }else{
        return Date.from(local_datetime!!.atZone(ZoneId.systemDefault()).toInstant())
    }
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
    tempFile.outputStream().use { fileOut ->
        inputStream?.copyTo(fileOut)
    }
    return tempFile
}

fun toSimpleDateString(date: Date) : String {
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}

class SearchViewModel : ViewModel() {
    val filter: MutableLiveData<String> = MutableLiveData("")
    val current_tab: MutableLiveData<Int> = MutableLiveData(0)
}

class ChatViewModel : ViewModel() {
    val filter: MutableLiveData<String> = MutableLiveData("")
    val current_tab: MutableLiveData<Int> = MutableLiveData(0)
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus ?: View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun formatDistance(distanceInMeters: BigDecimal): String {
    return if (distanceInMeters < BigDecimal(1000)) {
        "${distanceInMeters.setScale(0, RoundingMode.HALF_UP)} m"
    } else {
        val distanceInKm = distanceInMeters.divide(BigDecimal(1000))
        "${distanceInKm.setScale(2, RoundingMode.HALF_UP)} km"
    }
}

data class CardViewEventMap(val event: ClubEvent)