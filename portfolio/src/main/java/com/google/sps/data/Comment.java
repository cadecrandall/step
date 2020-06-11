package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/** Container to hold comment and metadata. */
public class Comment {

  public static final String USERNAME_PROPERTY = "username";
  public static final String TIMESTAMP_PROPERTY = "timestamp";
  public static final String MESSAGE_PROPERTY = "message";
  public static final String EMAIL_PROPERTY = "email";
  public static final String IMAGE_PROPERTY = "image";

  public static final String COMMENT_FORM_ID = "comment-message";
  public static final String USERNAME_FORM_ID = "username";
  public static final String IMAGE_FORM_ID = "image";


  public static final String COMMENT_ENTITY = "Comment";
  
  public final String username;
  public final long timestamp;
  public final String message;
  public final String email;
  public final String imageURL;

  public Comment(Entity entity) {
    this.username = (String) entity.getProperty(USERNAME_PROPERTY);
    this.timestamp = (long) entity.getProperty(TIMESTAMP_PROPERTY);
    this.message = (String) entity.getProperty(MESSAGE_PROPERTY);
    this.email = (String) entity.getProperty(EMAIL_PROPERTY);
    this.imageURL = (String) entity.getProperty(IMAGE_PROPERTY);
  }

  public Comment(HttpServletRequest request) {
    this.username = request.getParameter(USERNAME_FORM_ID);
    this.timestamp = System.currentTimeMillis();
    this.message = request.getParameter(COMMENT_FORM_ID);
    this.imageURL = getUploadedFileUrl(request, IMAGE_FORM_ID);

    // Email is added to entity in DataServlet.java
    this.email = "";
  }

  /** Return an Entity for DataStore */
  public Entity toEntity() {
    Entity commentEntity = new Entity(COMMENT_ENTITY);
    commentEntity.setProperty(USERNAME_PROPERTY, this.username);
    commentEntity.setProperty(MESSAGE_PROPERTY, this.message);
    commentEntity.setProperty(TIMESTAMP_PROPERTY, this.timestamp);
    commentEntity.setProperty(EMAIL_PROPERTY, this.email);
    commentEntity.setProperty(IMAGE_PROPERTY, this.imageURL);
    return commentEntity;
  }

  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get(IMAGE_FORM_ID);

    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Form contains only one file input located at index 0
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

    // To support running in Google Cloud Shell with AppEngine's devserver, we must use the relative
    // path to the image, rather than the path returned by imagesService which contains a host.
    try {
      URL url = new URL(imagesService.getServingUrl(options));
      return url.getPath();
    } catch (MalformedURLException e) {
      return imagesService.getServingUrl(options);
    }
  }
}