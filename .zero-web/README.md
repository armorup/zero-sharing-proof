# Share your app

Choose **Publish app** in the Zero sidebar to publish a completed version using your connected GitHub repository. The first time, Zero reviews the public destination, prepares the publishing files, asks VS Code for workflow permission if needed, and uses the normal reviewed upload. Later updates use the same button and keep the same page URL.

Wait for **Your app is live**. Then choose **Open app** or **Copy playable link**. You can retrieve the verified link later with **Zero: Copy Playable Link** or **Zero: Open Published App** in the command palette. This playable link is separate from the source repository link submitted in Pika.

If Zero reports one setup step, follow that specific action and choose **Recheck**. Zero preserves repository visibility, existing websites and custom domains. School policies, account permissions or private-repository plans can require help from your teacher or IT. The first publish confirms its destination. Later explicit Publish actions revalidate and update that approved site without another destination dialog. If optional simulation is configured for testing, it makes no upload or public website change.

Your completed version must be saved and uploaded. If Zero asks you to finish a change or upload current work, use its ordinary workflow and try **Publish app** again. A failed build preserves the previously published page. Zero shows a new link only after the exact publishing run succeeds and the public site serves that version.

## Export files or use itch.io

Choose **Export for web** from Zero's title menu or the command palette for a local browser build. Choose **Preview game** to try it on your computer, **Save ZIP** to save a copy, or **Share…** to choose a destination.

For itch.io, open https://itch.io/game/new in your own account. Choose **HTML**, upload the exported ZIP, and select **This file will be played in the browser**. Preview controls and sound before choosing visibility and saving the page. Copy the actual published game-page URL.

## Storage

The exported app includes its JavaScript, images, audio and runtime license/source material. It includes no Java installation or Maven compiler. Exporting replaces the last generated output in **.zero/web-export** and keeps it ignored by Git. A failed build preserves the last successful export. Compiler downloads are cached once per Zero installation.

Zero compiles and checks the browser app locally. GitHub Pages deploys those exact bytes; it does not commit generated game files into source history. A temporary draft release carries the app to Actions and is deleted after staging; interrupted cleanup is retried when you recheck. The deployment artifact expires after one day, while the live site remains available independently. GitHub retains its normal run/deployment history. Large image/audio assets usually dominate game size.

Hosting references: https://docs.github.com/en/pages/getting-started-with-github-pages/using-custom-workflows-with-github-pages and https://itch.io/docs/creators/html5
