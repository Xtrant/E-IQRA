import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_iqra.data.api.ApiConfig
import com.example.e_iqra.data.api.DataItem
import com.example.e_iqra.data.api.DoaResponseItem
import com.example.e_iqra.view.main.ui.dashboard.canvas.AnalyzeActivity
import com.example.e_iqra.view.main.ui.dashboard.canvas.CanvasActivity
import com.example.e_iqra.view.main.ui.quiz.QuizActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _doaList = MutableLiveData<List<DoaResponseItem>>()
    val doaList: LiveData<List<DoaResponseItem>> get() = _doaList

    private val _filteredDoaList = MutableLiveData<List<DoaResponseItem>>()
    val filteredDoaList: LiveData<List<DoaResponseItem>> get() = _filteredDoaList

    private val _asmaList = MutableLiveData<List<DataItem>>()
    val asmaList: LiveData<List<DataItem>> get() = _asmaList

    private val _slides = MutableLiveData<List<String>>()
    val slides: LiveData<List<String>> get() = _slides

    private val _descriptions = MutableLiveData<List<String>>()
    val descriptions: LiveData<List<String>> get() = _descriptions

    private val _buttonTexts = MutableLiveData<List<String>>()
    val buttonTexts: LiveData<List<String>> get() = _buttonTexts

    init {
        fetchDoaList()
        fetchAsmaList()
        updateSlides()
    }

    fun fetchDoaList() {
        val call = ApiConfig.getDoaApiService().getDoaList()
        call.enqueue(object : Callback<List<DoaResponseItem>> {
            override fun onResponse(call: Call<List<DoaResponseItem>>, response: Response<List<DoaResponseItem>>) {
                if (response.isSuccessful) {
                    val doaList = response.body()
                    _doaList.postValue(doaList ?: emptyList())
                } else {
                    _doaList.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<DoaResponseItem>>, t: Throwable) {
                _doaList.postValue(emptyList())
            }
        })
    }

    fun fetchAsmaList() {
        val call = ApiConfig.getAsmaApiService().getAsmaList()
        call.enqueue(object : Callback<List<DataItem>> {
            override fun onResponse(call: Call<List<DataItem>>, response: Response<List<DataItem>>) {
                if (response.isSuccessful) {
                    val asmaList = response.body()
                    _asmaList.postValue(asmaList ?: emptyList())
                } else {
                    _asmaList.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<DataItem>>, t: Throwable) {
                _asmaList.postValue(emptyList())
            }
        })
    }

    fun searchDoa(query: String) {
        val filteredList = _doaList.value?.filter {
            it.doa?.contains(query, true) ?: false ||
                    it.ayat?.contains(query, true) ?: false ||
                    it.latin?.contains(query, true) ?: false ||
                    it.artinya?.contains(query, true) ?: false
        } ?: emptyList()
        _filteredDoaList.postValue(filteredList)
    }

    private fun updateSlides() {
        _slides.postValue(getSlides())
        _descriptions.postValue(getDescriptions())
        _buttonTexts.postValue(getButtonTexts())
    }

    fun onSlideButtonClicked(position: Int, context: Context) {
        val intent: Intent? = when (position) {
            0 -> Intent(context, CanvasActivity::class.java)
            1 -> Intent(context, QuizActivity::class.java)
            2 -> Intent(context, AnalyzeActivity::class.java)
            else -> null
        }

        intent?.let {
            context.startActivity(it)
        }
    }

    private fun getSlides(): List<String> {
        return listOf(
            "Scan Hijaiyah",
            "Quiz",
            "New Course! Data Science Class"
        )
    }

    private fun getDescriptions(): List<String> {
        return listOf(
            "Pelajari secara langsung tentang huruf Hijaiyah",
            "Evaluasi pengetahuan anda",
            "Dive into the world of data science"
        )
    }

    private fun getButtonTexts(): List<String> {
        return listOf(
            "Coba Sekarang",
            "Coba Sekarang",
            "View Data Science Course"
        )
    }
}
