inDir = "out"
outDir = "C:\\Users\\Jackson\\Dropbox\\git\\TextAnalyzer\\out"

blogNames = sub("([a-zA-Z0-9-]+?)", "\\1", list.files(inDir))
#blogNames = c("murder-by-death")

for(i in 1:length(blogNames)) {
	blogName = blogNames[i]
	print(blogName)

	inDir2 = paste(inDir, "\\", blogName, sep="")
	outDir2 = paste(outDir, "\\", blogName, sep="")

	hourOfDayInFile = paste(inDir2, "\\hours.txt", sep="")
	hourOfDayOutFile = paste(outDir2, "\\hours.pdf", sep="")

	h1 = scan(hourOfDayInFile)
	h2 = c(h1, h1+24, h1-24)

	if (length(h1) > 2) {
		pdf(hourOfDayOutFile, width=15)
		plot(density(h2, adjust=0.05), xaxp=c(-1, 25, 1+24+1),
			xlim=c(-0, 24), main=blogName, sub=paste(length(h1), "posts"))
		dev.off()
	}

	dayOfWeekInFile = paste(inDir2, "\\days.txt", sep="")
	dayOfWeekOutFile = paste(outDir2, "\\days.pdf", sep="")

	d1 = scan(dayOfWeekInFile) %% 7
	d2 = c(d1, d1+7, d1-7)

	if (length(d1) > 2) {
		pdf(dayOfWeekOutFile, width=15)
		par(tck=-0.04)
		plot(density(d2, adjust=0.02), main=blogName, xlab="Day of Week (Saturday=0)", sub=paste(length(d1), "posts"),
			xlim=c(0, 7), xaxp=c(0, 7, 7))
		par(tck=par("tck")/2)
		#minor.tick(nx=4, tick.ratio=0.5)
		par(tck=par("tck")/5)
		#minor.tick(nx=24, tick.ratio=0.2)
		#plot(density(d2, adjust=0.02), main=blogName,
		#	sub=paste(length(x), "posts"), xaxp=c(0, 7, 7*24), xlim=c(0, 7))
		dev.off()
	}
}




#default <- "C:\\Users\\Jackson\\Dropbox\\Code\\Reblogs\\timestamps\\b41779690b83f182acc67d6388c7bac9.txt"
#fname <- choose.files(default=default, multi=FALSE)[1]
#output <- "C:\\Users\\Jackson\\Dropbox\\Code\\Reblogs\\timestamps\\b41779690b83f182acc67d6388c7bac9.pdf"
#blogNames = c("b41779690b83f182acc67d6388c7bac9", "thenordicks", "milady-midas")
#defaultInDir <- "C:\\Users\\Jackson\\Dropbox\\Code\\Reblogs\\timestamps"
#defaultOutDir <- "C:\\Users\\Jackson\\Dropbox\\Code\\Reblogs\\graphs"

#indir = choose.dir(default=defaultInDir, caption="Choose an input directory!");
#outdir = choose.dir(default=defaultOutDir, caption="Choose an output directory!");

