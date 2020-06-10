package com.google.sps.servlets;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/blobstore-upload-URL")
public class BlobstoreUploadURL extends HttpServlet {

  private static final String CONTENT_TYPE = "text/html;";
  private static final String DATA_SERVLET = "/data";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreService blob = BlobstoreServiceFactory.getBlobstoreService();

    // links to servlet that procesess form
    String uploadURL = blob.createUploadUrl(DATA_SERVLET);

    response.setContentType(CONTENT_TYPE);
    response.getWriter().println(uploadURL);
  }

}