package com.example.e_iqra.view.main.ui.dashboard.gallery

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.e_iqra.R

class GalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val imageView: ImageView = root.findViewById(R.id.imageView)

        arguments?.getString("image_uri")?.let {
            imageView.setImageURI(Uri.parse(it))
        }

        return root
    }
}
