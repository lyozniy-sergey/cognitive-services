<!DOCTYPE html>

<html>
    <head>
        <title>Welcome to cognitive service web application</title>
        <style type="text/css">
            .label {text-align: right}
            .error {color: red}
        </style>

    </head>

    <body>
        Welcome
        <p>
        <ul>
        <li><a href="/">Home</a></li>
        <li><a href="/face?subscriptionKey=6f5ae897edf040028fade24f123dba4f&returnFaceLandmarks=true&returnFaceAttributes=age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise&source=https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg">Get face detect</a></li>
        <li><a href="/recUser?userId=936ffba3-51b4-437c-bb25-302a302fb776&numberOfResults=150&subscriptionKey=84f59c16e2d74a179af9d8f8ac03119a&modelId=ef44e48c-e4f6-49d4-bf74-013834be248b">Get recommendation for user</a></li>
        <li><a href="/recItem?itemIds=23191,65652&numberOfResults=150&subscriptionKey=84f59c16e2d74a179af9d8f8ac03119a&modelId=ef44e48c-e4f6-49d4-bf74-013834be248b&minimalScore=0.0">Get recommendation for item</a></li>
        <li><a href="/create_model?subscriptionKey=84f59c16e2d74a179af9d8f8ac03119a&modelName=Model2">Create model</a></li>
        <li><a href="/c">Upload catalog</a></li>
        <li><a href="/u">Upload usage</a></li>
        <li><a href="/update_model?modelId=ef44e48c-e4f6-49d4-bf74-013834be248b&subscriptionKey=84f59c16e2d74a179af9d8f8ac03119a&activeBuildId=1661908">Update model</a></li>
        <li><a href="/create_build?modelId=ef44e48c-e4f6-49d4-bf74-013834be248b&subscriptionKey=84f59c16e2d74a179af9d8f8ac03119a&buildType=recommendation">Create recommendation build</a></li>
        <li><a href="/delete_build?modelId=ef44e48c-e4f6-49d4-bf74-013834be248b&buildId=1661906&subscriptionKey=84f59c16e2d74a179af9d8f8ac03119a">Delete build</a></li>
        <li><a href="/get_build?modelId=ef44e48c-e4f6-49d4-bf74-013834be248b&buildId=1661906&subscriptionKey=84f59c16e2d74a179af9d8f8ac03119a">Get build info</a></li>
    </body>

</html>
