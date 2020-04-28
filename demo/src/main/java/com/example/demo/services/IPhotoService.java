package com.example.demo.services;

import java.io.InputStream;
import java.util.Collection;

import com.example.demo.model.PhotoInfo;

public interface IPhotoService {

  Collection<PhotoInfo> getPhotosForCurrentUser(String username);

  InputStream loadPhoto(String id);
}
