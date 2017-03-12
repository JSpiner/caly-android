
DEPLOY_BRANCH=$1
DEPLOY_COMMIT=$2
DEPLOY_COMMIT_MESSAGE=$3

SLACK_KEY="xoxb-118976894853-""ypenVhWFgnlrfGmHXhrsImeq"
SLACK_TEXT="[ `$DEPLOY_BRANCH` | `$DEPLOY_COMMIT` ] *$DEPLOY_COMMIT_MESSAGE* ${TRAVIS_COMMIT_MESSAGE:-none} "
echo $SLACK_TEXT
echo $TRAVIS_BRANCH

echo $DEPLOY_BRANCH
echo $DEPLOY_COMMIT
echo $DEPLOY_COMMIT_MESSAGE
curl \
  -F "token=$SLACK_KEY" \
  -F "channels=deploy-android" \
  -F "initial_comment=$SLACK_TEXT" \
  -F "file=@app/build/outputs/apk/app-debug.apk" \
  https://slack.com/api/files.upload