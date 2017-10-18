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
        <li><a href="/face?subscriptionKey=4b28731ec9694254855d154f1cc1f31a&returnFaceLandmarks=true&returnFaceAttributes=age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise&source=https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg">Get face detect</a></li>
        <li><a href="/recUser?userId=936ffba3-51b4-437c-bb25-302a302fb776&numberOfResults=150&subscriptionKey=abe7eea3a1e94cb4bf150735292971ce&modelId=1fa58243-75ab-4e6c-97fc-9cdf96db3f76">Get recommendation for user</a></li>
        <li><a href="/recItem?itemIds=23191,65652&numberOfResults=150&subscriptionKey=abe7eea3a1e94cb4bf150735292971ce&modelId=1fa58243-75ab-4e6c-97fc-9cdf96db3f76&minimalScore=0.0">Get recommendation for item</a></li>
        <li><a href="/create_model?subscriptionKey=abe7eea3a1e94cb4bf150735292971ce&modelName=Model2">Create model</a></li>
        <li><a href="/c">Upload catalog</a></li>
        <li><a href="/u">Upload usage</a></li>
        <li><a href="/update_model?modelId=1fa58243-75ab-4e6c-97fc-9cdf96db3f76&subscriptionKey=abe7eea3a1e94cb4bf150735292971ce&activeBuildId=1661908">Update model</a></li>
        <li><a href="/create_build?modelId=1fa58243-75ab-4e6c-97fc-9cdf96db3f76&subscriptionKey=abe7eea3a1e94cb4bf150735292971ce&buildType=recommendation">Create recommendation build</a></li>
        <li><a href="/delete_build?modelId=1fa58243-75ab-4e6c-97fc-9cdf96db3f76&buildId=1661906&subscriptionKey=abe7eea3a1e94cb4bf150735292971ce">Delete build</a></li>
        <li><a href="/get_build?modelId=1fa58243-75ab-4e6c-97fc-9cdf96db3f76&buildId=1661906&subscriptionKey=abe7eea3a1e94cb4bf150735292971ce">Get build info</a></li>
    </body>

</html>
