# SubAdjust
A simple program for adjusting the timing in subtitle files (.srt and .ass)

Linear adjustment simply means shifting everything by the same amount. 
Entered value should be negative if subtitles should appear earlier and vice-versa. 

The start time condition applies the change only to subtitles after the given time. 
This is primarily useful for videos that were put together from different files or where parts (like commercials) were cut out, 
which can lead to the subtitle track being out of sync after reaching the point where the cut occured. 
If multiple such cuts are present, the program has to be used repeatedly, once per cut. 

Proportional Adjustment is needed when subtitles have a different playback speed than the video. 
Simply enter 2 sets of current time of subtitle appearance and corrected time of appearance.
Ideally, both chosen instances should be far each other for higher precision. 

