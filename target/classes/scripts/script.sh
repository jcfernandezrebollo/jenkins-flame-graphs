#! /bin/sh
if test $# -eq 6 
then
	echo "$# arguments"
	echo "args for flamegraph $1"
	echo "jobname $2"
	echo "buildname $3"
	echo "seconds for perf record $4"
	echo "type of flame $6"


	case $6 in

		"cpuFlame")
			sudo perf record -F 99 -a -g -- sleep $4 &
			$1
			sleep $5
			sudo perf script -i perf.data | ./src/main/resources/scripts/stackcollapse-perf.pl | ./src/main/resources/scripts/flamegraph.pl  > $2"-"$3.svg
		;;
		"differentialFlame")
			sudo perf record -F 99 -a -g -- sleep $4 &
			$1
			sleep $5
			sudo perf script -i perf.data > out.stacks1
			./src/main/resources/scripts/stackcollapse-perf.pl out.stacks1 > out.folded1
			sleep 120
			mv perf.data work/jobs/$2/builds/$3
			sudo perf record -F 99 -a -g -- sleep $4 &
			$1
			sleep $5
			sudo perf script -i perf.data > out.stacks2
			./src/main/resources/scripts/stackcollapse-perf.pl out.stacks2 > out.folded2	
			./src/main/resources/scripts/difffolded.pl out.folded1 out.folded2 | ./src/main/resources/scripts/flamegraph.pl  > $2"-"$3.svg
	
			mv out.stacks1 work/jobs/$2/builds/$3
			mv out.stacks2 work/jobs/$2/builds/$3
			mv out.folded1 work/jobs/$2/builds/$3
			mv out.folded2 work/jobs/$2/builds/$3
		;;
		*) 
			echo "UNKNOWN FLAMEGRAPH TYPE" 1>&2
			mv outError.txt work/jobs/$2/builds/$3
			mv outNormal.txt work/jobs/$2/builds/$3
			exit 1			
		;;


	esac

	mv perf.data work/jobs/$2/builds/$3
	mv outError.txt work/jobs/$2/builds/$3
	mv outNormal.txt work/jobs/$2/builds/$3
	mv $2"-"$3.svg src/main/webapp
	
else
	echo "WRONG NUMBER OF ARGUMENTS" 1<&2
	mv outError.txt work/jobs/$2/builds/$3
	mv outNormal.txt work/jobs/$2/builds/$3
	exit 2
fi
exit 0

