VIDEO = video
NOVIDEO =  no-video
SOURCE = ./main.tex

all: build clean

build: build$(NOVIDEO) build$(VIDEO)

$(VIDEO): build$(VIDEO) clean

$(NOVIDEO): build$(NOVIDEO) clean

build$(VIDEO): $(SOURCE)
	pdflatex -jobname=tp2-slides-$(VIDEO) $(SOURCE) '\def\OUTPUT{$(VIDEO)} \input{$(SOURCE)}' $(SOURCE) > /dev/null

build$(NOVIDEO) :$(SOURCE)
	pdflatex -jobname=tp2-slides-$(NOVIDEO) '\def\OUTPUT{$(NOVIDEO)} \input{$(SOURCE)}' $(SOURCE) > /dev/null

clean: 
	rm -rf *.aux *.log *.out *.fls *.gz *.fdb_latexmk