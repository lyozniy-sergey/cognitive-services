# Cognitive Service

Cognitive Services are a set of machine learning algorithms that Microsoft has developed to solve problems in the field of Artificial Intelligence (AI). The goal of Cognitive Services is to democratize AI by packaging it into discrete components that are easy for developers to use in their own apps. Web and Universal Windows Platform developers can consume these algorithms through standard REST calls over the Internet to the Cognitive Services APIs.
 
The Cognitive Services APIs are grouped into five categories…

Vision—analyze images and videos for content and other useful information.
Speech—tools to improve speech recognition and identify the speaker.
Language—understanding sentences and intent rather than just words.
Knowledge—tracks down research from scientific journals for you.
Search—applies machine learning to web searches.

Cognitive Services Vision APIs

The Vision APIs are broken out into five groups of tasks…

Computer Vision—Distill actionable information from images.
Content Moderator—Automatically moderate text, images and videos for profanity and inappropriate content.
Emotion—Analyze faces to detect a range of moods.
Face—identify faces and similarities between faces.
Video—Analyze, edit and process videos within your app.
Recommendation API, Predict what your customers want and increase the searchability of your catalog

There is a controller to make a cognitive service call
 
 For example:

 Get user-to-item recommendations:
 http://localhost:4567/recUser?uriBase=https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/b1a1e954-ab2e-4da3-9aaa-6ecee7b08166/recommend/user&userId=00034001CFAC09B3&numberOfResults=150&subscriptionKey=abe7eea3a1e94cb4bf150735292971ce

 Detect faces in images with Face API using Java:
 http://localhost:4567/face?uriBase=https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect&subscriptionKey=4b28731ec9694254855d154f1cc1f31a&returnFaceLandmarks=true&returnFaceAttributes=age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise&source=https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg