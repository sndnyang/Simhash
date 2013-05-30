#!/usr/bin/env python

from collections import defaultdict

min_fp_dict = defaultdict(float)
min_red_dict = defaultdict(float)
min_fset_dict = defaultdict(float)
min_ensd_dict = defaultdict(float)

min_dict = [min_fp_dict, min_red_dict,min_fset_dict,min_ensd_dict]

max_fp_dict = defaultdict(float)
max_red_dict = defaultdict(float)
max_fset_dict = defaultdict(float)
max_ensd_dict = defaultdict(float)

max_dict = [max_fp_dict, max_red_dict,max_fset_dict,max_ensd_dict]

avg_fp_dict = defaultdict(float)
avg_red_dict = defaultdict(float)
avg_fset_dict = defaultdict(float)
avg_ensd_dict = defaultdict(float)

avg_dict = [avg_fp_dict, avg_red_dict,avg_fset_dict,avg_ensd_dict]

cou_dict = defaultdict(int)
if __name__ == '__main__':
    file_name = "E:\yangxiulong\Simhash\output\DistToPrecise.csv"
    csv = open(file_name)
    cur_dist = -1
    flag = 0
    for line in csv.readlines()[2:]:
        nums = line.split(',')
        dis = int(nums[0])
        if cur_dist != dis:
            flag = 0
            cur_dist = dis
        
        for i in range(1, 5):

            temp = abs(float(nums[i]))
                
            avg_dict[i-1][dis] += temp
            
            if temp > max_dict[i-1][dis]:
                max_dict[i-1][dis] = temp

            if (flag == 0 and min_dict[i-1][dis] == 0) or min_dict[i-1][dis] > temp:
                min_dict[i-1][dis] = temp
        
        cou_dict[dis] += 1
        flag = 1

    for j in range(0, 4):
        for i in range(0, 21):
            print i,
            print ',',
            print min_dict[j][i],
            print ',',
            print max_dict[j][i],
            print ',',
            print avg_dict[j][i] / cou_dict[i]
        print

        
        
        
        
