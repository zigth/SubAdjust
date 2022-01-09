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
Ideally, both chosen instances should be far from each other for higher precision. 

To prevent accidental overwriting of files this program does not overwrite the given files but instead places output in a new file named: xxx2.srt, where xxx is the original file's name. 
 
Example usage cases:
1) subtitles appear 1.5 seconds to late:
- enter file name 
- when prompted for linear adjustment: 
	'y'
- when prompted for hundreths of second:
	'-150'
- when prompted for start time limitation:
	'n'

2) subtitle are correct up to 25min 13sec into the video at which point they are 2.4 sec early:
- enter file name 
- when prompted for linear adjustment: 
	'y'
- when prompted for hundreths of second:
	'240'
- when prompted for start time limitation:
	'y'
- when prompted for time:
	'00:25:13'

3) subtitles are off because of playback speed and there is also a constant offset. Earliest subtitle appearing at 1:56 should appear at 1:49 and last subtitle appearing 1:37:11.8 should appear at 1:33:10.4 :
- enter file name 
- when prompted for linear adjustment: 
	'n'
- when prompted for proportional adjustment:
	'y'
- when prompted for current time > correct time for first subtitle:
	'00:01:56:00>00:01:49:00'
- when prompted for current time > correct time for last subtitle:
	'01:37:11:80>01:33:10:40'

